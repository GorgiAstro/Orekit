/* Copyright 2002-2022 CS GROUP
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
package org.orekit.files.ccsds.ndm.odm.ocm;

import org.junit.Test;
import org.orekit.files.ccsds.ndm.AbstractWriterTest;
import org.orekit.files.ccsds.ndm.ParsedUnitsBehavior;
import org.orekit.files.ccsds.ndm.ParserBuilder;
import org.orekit.files.ccsds.ndm.WriterBuilder;
import org.orekit.files.ccsds.section.Header;
import org.orekit.files.ccsds.section.Segment;
import org.orekit.utils.Constants;

public class OcmWriterTest extends AbstractWriterTest<Header, Segment<OcmMetadata, OcmData>, Ocm> {

    protected OcmParser getParser() {
        return new ParserBuilder().
               withParsedUnitsBehavior(ParsedUnitsBehavior.STRICT_COMPLIANCE).
               withMu(Constants.EIGEN5C_EARTH_MU).
               buildOcmParser();
    }

    protected OcmWriter getWriter() {
        return new WriterBuilder().buildOcmWriter();
    }

    @Test
    public void testWriteExample1() {
        doTest("/ccsds/odm/ocm/OCMExample1.txt");
    }

    @Test
    public void testWriteKvnExample2() {
        doTest("/ccsds/odm/ocm/OCMExample2.txt");
    }

    @Test
    public void testWriteXmlExample2() {
        doTest("/ccsds/odm/ocm/OCMExample2.xml");
    }

    @Test
    public void testWriteExample3() {
        doTest("/ccsds/odm/ocm/OCMExample3.txt");
    }

    @Test
    public void testWriteExample4() {
        doTest("/ccsds/odm/ocm/OCMExample4.txt");
    }

}
