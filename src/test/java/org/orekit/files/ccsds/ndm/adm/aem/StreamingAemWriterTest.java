/* Copyright 2002-2021 CS GROUP
 * Licensed to CS GROUP (CS) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * CS licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.orekit.files.ccsds.ndm.adm.aem;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.hipparchus.geometry.euclidean.threed.Rotation;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.orekit.Utils;
import org.orekit.attitudes.AttitudeProvider;
import org.orekit.attitudes.InertialProvider;
import org.orekit.data.DataContext;
import org.orekit.data.NamedData;
import org.orekit.files.ccsds.ndm.adm.AttitudeEndPoints;
import org.orekit.files.ccsds.section.Header;
import org.orekit.files.ccsds.utils.CCSDSBodyFrame;
import org.orekit.files.ccsds.utils.CCSDSFrame;
import org.orekit.files.ccsds.utils.CcsdsTimeScale;
import org.orekit.files.ccsds.utils.generation.KVNGenerator;
import org.orekit.files.ccsds.utils.lexical.KVNLexicalAnalyzer;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.TimeStampedAngularCoordinates;


public class StreamingAemWriterTest {

    private static final double QUATERNION_PRECISION = 1e-5;
    private static final double DATE_PRECISION = 1e-3;

    /** Set Orekit data. */
    @Before
    public void setUp() {
        Utils.setDataRoot("regular-data");
    }

    /**
     * Check reading and writing an AEM both with and without using the step handler
     * methods.
     */
    @Test
    public void testWriteAemStepHandler() throws Exception {

        // Create a list of files
        List<String> files = Arrays.asList("/ccsds/adm/aem/AEMExample7.txt");
        for (final String ex : files) {

            // Reference AEM file
            final NamedData source0 = new NamedData(ex, () -> getClass().getResourceAsStream(ex));
            AEMParser parser = new AEMParser(IERSConventions.IERS_2010, true, DataContext.getDefault(), null, 1);
            AEMFile aemFile  = new KVNLexicalAnalyzer(source0).accept(parser);

            // Satellite attitude ephemeris as read from the reference file
            AEMSegment ephemerisBlock = aemFile.getSegments().get(0);

            // Meta data are extracted from the reference file
            String            originator   = aemFile.getHeader().getOriginator();
            String            objectName   = ephemerisBlock.getMetadata().getObjectName();
            String            objectID     = ephemerisBlock.getMetadata().getObjectID();
            String            headerCmt    = aemFile.getHeader().getComments().get(0);
            AttitudeEndPoints ep           = ephemerisBlock.getMetadata().getEndPoints();
            boolean           attitudeDir  = ep.isExternal2Local();
            CCSDSFrame        refFrameA    = ep.getExternalFrame();
            CCSDSBodyFrame    refFrameB    = ep.getLocalFrame();
            AEMAttitudeType   attitudeType = ephemerisBlock.getMetadata().getAttitudeType();
            boolean           isFirst      = ephemerisBlock.getMetadata().isFirst();

            // Initialize the header and metadata
            // Here, we use only one data segment.
            Header header = new Header();
            header.setOriginator(originator);
            header.addComment(headerCmt);

            AEMMetadata metadata = new AEMMetadata(1);
            metadata.setTimeSystem(CcsdsTimeScale.UTC);
            metadata.setObjectID(objectID);
            metadata.setObjectName("will be overwritten");
            metadata.setAttitudeType(attitudeType);
            metadata.setIsFirst(isFirst);
            metadata.getEndPoints().setExternalFrame(refFrameA);
            metadata.getEndPoints().setLocalFrame(refFrameB);
            metadata.getEndPoints().setExternal2Local(attitudeDir);
            metadata.setStartTime(AbsoluteDate.PAST_INFINITY);  // will be overwritten at propagation start
            metadata.setStopTime(AbsoluteDate.FUTURE_INFINITY); // will be overwritten at propagation start
            final AEMWriter aemWriter = new AEMWriter(IERSConventions.IERS_2010, DataContext.getDefault(), header, metadata);

            StringBuilder buffer = new StringBuilder();
            StreamingAemWriter writer = new StreamingAemWriter(new KVNGenerator(buffer, AEMWriter.DEFAULT_FILE_NAME),
                                                               aemWriter);
            aemWriter.getMetadata().setObjectName(objectName);

            // Initialize a Keplerian propagator with an Inertial attitude provider
            // It is expected that all attitude data lines will have the same value
            StreamingAemWriter.SegmentWriter segment = writer.newSegment();
            KeplerianPropagator propagator =
                            createPropagator(ephemerisBlock.getStart(),
                                             new InertialProvider(ephemerisBlock.getAngularCoordinates().get(0).getRotation(),
                                                                  FramesFactory.getEME2000()));

            // We propagate 60 seconds after the start date with a step equals to 10.0 seconds
            // It is expected to have an attitude data block containing 7 data lines
            double step = 10.0;
            propagator.setMasterMode(step, segment);
            propagator.propagate(ephemerisBlock.getStart().shiftedBy(60.0));

            // Generated AEM file
            final NamedData source1 = new NamedData("buffer",
                                                   () -> new ByteArrayInputStream(buffer.toString().getBytes(StandardCharsets.UTF_8)));
            AEMFile generatedAemFile = new KVNLexicalAnalyzer(source1). accept(parser);

            // There is only one attitude ephemeris block
            Assert.assertEquals(1, generatedAemFile.getSegments().size());
            AEMSegment attitudeBlocks = generatedAemFile.getSegments().get(0);
            // There are 7 data lines in the attitude ephemeris block
            List<? extends TimeStampedAngularCoordinates> ac  = attitudeBlocks.getAngularCoordinates();
            Assert.assertEquals(7, ac.size());

            // Verify
            for (int i = 0; i < 7; i++) {
                Assert.assertEquals(step * i, ac.get(i).getDate().durationFrom(ephemerisBlock.getStart()), DATE_PRECISION);
                Rotation rot = ac.get(i).getRotation();
                Assert.assertEquals(0.68427, rot.getQ0(), QUATERNION_PRECISION);
                Assert.assertEquals(0.56748, rot.getQ1(), QUATERNION_PRECISION);
                Assert.assertEquals(0.03146, rot.getQ2(), QUATERNION_PRECISION);
                Assert.assertEquals(0.45689, rot.getQ3(), QUATERNION_PRECISION);
            }

        }

    }

    /**
     * Create a Keplerian propagator.
     * @param date reference date
     * @param attitudeProv attitude provider
     * @return a Keplerian propagator
     */
    private KeplerianPropagator createPropagator(AbsoluteDate date,
                                                 AttitudeProvider attitudeProv) {
        Vector3D position = new Vector3D(-29536113.0, 30329259.0, -100125.0);
        Vector3D velocity = new Vector3D(-2194.0, -2141.0, -8.0);
        PVCoordinates pvCoordinates = new PVCoordinates( position, velocity);
        double mu = 3.9860047e14;

        CartesianOrbit p = new CartesianOrbit(pvCoordinates, FramesFactory.getEME2000(), date, mu);

        return new KeplerianPropagator(p, attitudeProv);
    }

}
