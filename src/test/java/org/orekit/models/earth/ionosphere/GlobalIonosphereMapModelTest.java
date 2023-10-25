/* Copyright 2002-2023 CS GROUP
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
package org.orekit.models.earth.ionosphere;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hipparchus.CalculusFieldElement;
import org.hipparchus.Field;
import org.hipparchus.util.Binary64Field;
import org.hipparchus.util.FastMath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.orekit.Utils;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.errors.OrekitException;
import org.orekit.errors.OrekitMessages;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.TopocentricFrame;
import org.orekit.gnss.Frequency;
import org.orekit.orbits.FieldKeplerianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngleType;
import org.orekit.propagation.FieldSpacecraftState;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FieldAbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;

public class GlobalIonosphereMapModelTest {

    private static double epsilonParser = 1.0e-16;
    private static double epsilonDelay  = 0.001;
    private SpacecraftState state;
    private OneAxisEllipsoid earth;
    private GlobalIonosphereMapModel model;

    @BeforeEach
    public void setUp() {
        Utils.setDataRoot("regular-data:ionex");
        model = new GlobalIonosphereMapModel("gpsg0150.19i");
        final Orbit orbit = new KeplerianOrbit(24464560.0, 0.0, 1.122138, 1.10686, 1.00681,
                                               0.048363, PositionAngleType.MEAN,
                                               FramesFactory.getEME2000(),
                                               new AbsoluteDate(2019, 1, 14, 23, 59, 59.0, TimeScalesFactory.getUTC()),
                                               Constants.WGS84_EARTH_MU);
        state = new SpacecraftState(orbit);
        earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                                     Constants.WGS84_EARTH_FLATTENING,
                                     FramesFactory.getITRF(IERSConventions.IERS_2010, true));
    }

    @Test
    public void testTEC() {
        final double latitude  = FastMath.toRadians(30.0);
        final double longitude = FastMath.toRadians(-130.0);
        final double tec = model.getTEC(new AbsoluteDate(2019, 1, 15, 3, 43, 12.0, TimeScalesFactory.getUTC()),
                                        new GeodeticPoint(latitude, longitude, 0.0));
        Assertions.assertEquals(9.592, tec, epsilonDelay);
    }

    @Test
    public void testFieldTEC() {
        doTestFieldTEC(Binary64Field.getInstance());
    }

    private <T extends CalculusFieldElement<T>> void doTestFieldTEC(final Field<T> field) {
        final double latitude  = FastMath.toRadians(30.0);
        final double longitude = FastMath.toRadians(-130.0);
        final T tec = model.getTEC(new FieldAbsoluteDate<>(field, 2019, 1, 15, 3, 43, 12.0, TimeScalesFactory.getUTC()),
                                   new GeodeticPoint(latitude, longitude, 0.0));
        Assertions.assertEquals(9.592, tec.getReal(), epsilonDelay);
    }

    @Test
    public void testDelay() {
        final double latitude  = FastMath.toRadians(30.0);
        final double longitude = FastMath.toRadians(-130.0);
        try {
            final Method pathDelay = GlobalIonosphereMapModel.class.getDeclaredMethod("pathDelayAtIPP",
                                                                                      AbsoluteDate.class,
                                                                                      GeodeticPoint.class,
                                                                                      Double.TYPE, Double.TYPE);
            pathDelay.setAccessible(true);
            final double delay = (Double) pathDelay.invoke(model,
                                                           new AbsoluteDate(2019, 1, 15, 3, 43, 12.0, TimeScalesFactory.getUTC()),
                                                           new GeodeticPoint(latitude, longitude, 0.0),
                                                           0.5 * FastMath.PI, Frequency.G01.getMHzFrequency() * 1.0e6);
            Assertions.assertEquals(1.557, delay, epsilonDelay);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testFieldDelay() {
        doTestFieldDelay(Binary64Field.getInstance());
    }

    private <T extends CalculusFieldElement<T>> void doTestFieldDelay(final Field<T> field) {
        final T zero = field.getZero();
        final double latitude  = FastMath.toRadians(30.0);
        final double longitude = FastMath.toRadians(-130.0);
        try {
            final Method pathDelay = GlobalIonosphereMapModel.class.getDeclaredMethod("pathDelayAtIPP",
                                                                                      FieldAbsoluteDate.class,
                                                                                      GeodeticPoint.class,
                                                                                      CalculusFieldElement.class,
                                                                                      Double.TYPE);
            pathDelay.setAccessible(true);
            @SuppressWarnings("unchecked")
            final T delay = (T) pathDelay.invoke(model,
                                                 new FieldAbsoluteDate<>(field, 2019, 1, 15, 3, 43, 12.0, TimeScalesFactory.getUTC()),
                                                 new GeodeticPoint(latitude, longitude, 0.0),
                                                 zero.add(0.5 * FastMath.PI),
                                                 Frequency.G01.getMHzFrequency() * 1.0e6);
            Assertions.assertEquals(1.557, delay.getReal(), epsilonDelay);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                        IllegalArgumentException | InvocationTargetException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testSpacecraftState() {
        double a = 7187990.1979844316;
        double e = 0.5e-4;
        double i = FastMath.toRadians(98.01);
        double omega = FastMath.toRadians(131.88);
        double OMEGA = FastMath.toRadians(252.24);
        double lv = FastMath.toRadians(250.00);

        AbsoluteDate date = new AbsoluteDate(2019, 1, 15, 3, 43, 12.0, TimeScalesFactory.getUTC());
        final SpacecraftState state =
                        new SpacecraftState(new KeplerianOrbit(a, e, i, omega, OMEGA, lv, PositionAngleType.TRUE,
                                                               FramesFactory.getEME2000(), date,
                                                               Constants.EIGEN5C_EARTH_MU));
        final TopocentricFrame topo = new TopocentricFrame(earth,
                                                           new GeodeticPoint(FastMath.toRadians(20.5236),
                                                                             FastMath.toRadians(85.7881),
                                                                             36.0),
                                                           "Cuttack");
        final double delay = model.pathDelay(state, topo, Frequency.G01.getMHzFrequency() * 1.0e6, null);
        Assertions.assertEquals(2.810, delay, epsilonDelay);

        // the delay at station longitude is different, due to IPP
        try {
            final Method pathDelay = GlobalIonosphereMapModel.class.getDeclaredMethod("pathDelayAtIPP",
                                                                                      AbsoluteDate.class,
                                                                                      GeodeticPoint.class,
                                                                                      Double.TYPE, Double.TYPE);
            pathDelay.setAccessible(true);
            final double delayIPP = (Double) pathDelay.invoke(model, date, topo.getPoint(),
                                                              0.5 * FastMath.PI,
                                                              Frequency.G01.getMHzFrequency() * 1.0e6);
            Assertions.assertEquals(2.173, delayIPP, epsilonDelay);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException ex) {
            Assertions.fail(ex);
        }
    }

    @Test
    public void testAboveIono() {
        double a = 7187990.1979844316;
        double e = 0.5e-4;
        double i = FastMath.toRadians(98.01);
        double omega = FastMath.toRadians(131.88);
        double OMEGA = FastMath.toRadians(252.24);
        double lv = FastMath.toRadians(250.00);

        AbsoluteDate date = new AbsoluteDate(2019, 1, 15, 3, 43, 12.0, TimeScalesFactory.getUTC());
        final SpacecraftState state =
                        new SpacecraftState(new KeplerianOrbit(a, e, i, omega, OMEGA, lv, PositionAngleType.TRUE,
                                                               FramesFactory.getEME2000(), date,
                                                               Constants.EIGEN5C_EARTH_MU));
        final TopocentricFrame topo = new TopocentricFrame(earth,
                                                           new GeodeticPoint(FastMath.toRadians(20.5236),
                                                                             FastMath.toRadians(85.7881),
                                                                             650000.0),
                                                           "very-high");
        final double delay = model.pathDelay(state, topo, Frequency.G01.getMHzFrequency() * 1.0e6, null);
        Assertions.assertEquals(0.0, delay, epsilonDelay);

    }

    @Test
    public void testSpacecraftStateField() {
        doTestSpacecraftStateField(Binary64Field.getInstance());
    }

    private <T extends CalculusFieldElement<T>> void doTestSpacecraftStateField(final Field<T> field) {
        T a = field.getZero().newInstance(7187990.1979844316);
        T e = field.getZero().newInstance(0.5e-4);
        T i = FastMath.toRadians(field.getZero().newInstance(98.01));
        T omega = FastMath.toRadians(field.getZero().newInstance(131.88));
        T OMEGA = FastMath.toRadians(field.getZero().newInstance(252.24));
        T lv = FastMath.toRadians(field.getZero().newInstance(250.00));

        FieldAbsoluteDate<T> date = new FieldAbsoluteDate<>(field,
                                                            new AbsoluteDate(2019, 1, 15, 3, 43, 12.0, TimeScalesFactory.getUTC()));
        final FieldSpacecraftState<T> state =
                        new FieldSpacecraftState<>(new FieldKeplerianOrbit<>(a, e, i, omega, OMEGA, lv, PositionAngleType.TRUE,
                                                                             FramesFactory.getEME2000(), date,
                                                                             field.getZero().newInstance(Constants.EIGEN5C_EARTH_MU)));
        final TopocentricFrame topo = new TopocentricFrame(earth,
                                                           new GeodeticPoint(FastMath.toRadians(20.5236),
                                                                             FastMath.toRadians(85.7881),
                                                                             36.0),
                                                           "Cuttack");
        final T delay = model.pathDelay(state, topo, Frequency.G01.getMHzFrequency() * 1.0e6, null);
        Assertions.assertEquals(2.810, delay.getReal(), epsilonDelay);

        // the delay at station longitude is different, due to IPP
        try {
            final Method pathDelay = GlobalIonosphereMapModel.class.getDeclaredMethod("pathDelayAtIPP",
                                                                                      FieldAbsoluteDate.class,
                                                                                      GeodeticPoint.class,
                                                                                      CalculusFieldElement.class,
                                                                                      Double.TYPE);
            pathDelay.setAccessible(true);
            @SuppressWarnings("unchecked")
            final T delayIPP = (T) pathDelay.invoke(model, date, topo.getPoint(),
                                                    field.getZero().newInstance(0.5 * FastMath.PI),
                                                    Frequency.G01.getMHzFrequency() * 1.0e6);
            Assertions.assertEquals(2.173, delayIPP.getReal(), epsilonDelay);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException ex) {
            Assertions.fail(ex);
        }
    }

    @Test
    public void testAboveIonoField() {
        doTestAboveIonoField(Binary64Field.getInstance());
    }

    private <T extends CalculusFieldElement<T>> void doTestAboveIonoField(final Field<T> field) {
        T a = field.getZero().newInstance(7187990.1979844316);
        T e = field.getZero().newInstance(0.5e-4);
        T i = FastMath.toRadians(field.getZero().newInstance(98.01));
        T omega = FastMath.toRadians(field.getZero().newInstance(131.88));
        T OMEGA = FastMath.toRadians(field.getZero().newInstance(252.24));
        T lv = FastMath.toRadians(field.getZero().newInstance(250.00));

        FieldAbsoluteDate<T> date = new FieldAbsoluteDate<>(field,
                                                            new AbsoluteDate(2019, 1, 15, 3, 43, 12.0, TimeScalesFactory.getUTC()));
        final FieldSpacecraftState<T> state =
                        new FieldSpacecraftState<>(new FieldKeplerianOrbit<>(a, e, i, omega, OMEGA, lv, PositionAngleType.TRUE,
                                                                             FramesFactory.getEME2000(), date,
                                                                             field.getZero().newInstance(Constants.EIGEN5C_EARTH_MU)));
        final TopocentricFrame topo = new TopocentricFrame(earth,
                                                           new GeodeticPoint(FastMath.toRadians(20.5236),
                                                                             FastMath.toRadians(85.7881),
                                                                             650000.0),
                                                           "very-high");
        final T delay = model.pathDelay(state, topo, Frequency.G01.getMHzFrequency() * 1.0e6, null);
        Assertions.assertEquals(0.0, delay.getReal(), epsilonDelay);

    }

    @Test
    public void testParser() {

        // Commons parameters
        AbsoluteDate date = new AbsoluteDate(2019, 1, 15, 0, 0, 0.0, TimeScalesFactory.getUTC());
        final double latitude = FastMath.toRadians(45.0);

        double longitude1;
        double longitude2;

        // Test longitude = 181° and longitude = -179°
        longitude1 = FastMath.toRadians(181.0);
        longitude2 = FastMath.toRadians(-179.0);

        Assertions.assertEquals(model.getTEC(date, new GeodeticPoint(latitude, longitude1, 0.0)), model.getTEC(date, new GeodeticPoint(latitude, longitude2, 0.0)), epsilonParser);

        // Test longitude = 180° and longitude = -180°
        longitude1 = FastMath.toRadians(180.0);
        longitude2 = FastMath.toRadians(-180.0);

        Assertions.assertEquals(model.getTEC(date, new GeodeticPoint(latitude, longitude1, 0.0)), model.getTEC(date, new GeodeticPoint(latitude, longitude2, 0.0)), epsilonParser);

        // Test longitude = 0° and longitude = 360°
        longitude1 =  FastMath.toRadians(0.);
        longitude2 =  FastMath.toRadians(360.0);

        Assertions.assertEquals(model.getTEC(date, new GeodeticPoint(latitude, longitude1, 0.0)), model.getTEC(date, new GeodeticPoint(latitude, longitude2, 0.0)), epsilonParser);

    }

    @Test
    public void testCorruptedFileBadData() {
        final String fileName = "corrupted-bad-data-gpsg0150.19i";
        final double latitude  = FastMath.toRadians(30.0);
        final double longitude = FastMath.toRadians(-130.0);

        try {
            GlobalIonosphereMapModel corruptedModel = new GlobalIonosphereMapModel(fileName);
            corruptedModel.getTEC(new AbsoluteDate(2019, 1, 15, 0, 0, 0.0, TimeScalesFactory.getUTC()),
                         new GeodeticPoint(latitude, longitude, 0.0));
            Assertions.fail("An exception should have been thrown");

        } catch (OrekitException oe) {
            Assertions.assertEquals(OrekitMessages.UNABLE_TO_PARSE_LINE_IN_FILE, oe.getSpecifier());
        }
    }

    @Test
    public void testEarlierDate() {
        final double latitude  = FastMath.toRadians(60.0);
        final double longitude = FastMath.toRadians(-130.0);
        final GeodeticPoint point = new GeodeticPoint(latitude, longitude, 0.0);

        try {
            model.pathDelay(state, new TopocentricFrame(earth, point, null),
                            Frequency.G01.getMHzFrequency() * 1.0e6,
                            model.getParameters(new AbsoluteDate()));
            Assertions.fail("An exception should have been thrown");
        } catch (OrekitException oe) {
            Assertions.assertEquals(OrekitMessages.NO_TEC_DATA_IN_FILE_FOR_DATE, oe.getSpecifier());
        }
    }

    @Test
    public void testFieldEarlierDate() {
        doTestFieldEarlierDate(Binary64Field.getInstance());
    }

    private <T extends CalculusFieldElement<T>> void doTestFieldEarlierDate(final Field<T> field) {
        final double latitude  = FastMath.toRadians(60.0);
        final double longitude = FastMath.toRadians(-130.0);
        final GeodeticPoint point = new GeodeticPoint(latitude, longitude, 0.0);

        try {
            model.pathDelay(new FieldSpacecraftState<>(field, state), new TopocentricFrame(earth, point, null),
                            Frequency.G01.getMHzFrequency() * 1.0e6,
                            model.getParameters(field, new FieldAbsoluteDate<>(field)));
            Assertions.fail("An exception should have been thrown");
        } catch (OrekitException oe) {
            Assertions.assertEquals(OrekitMessages.NO_TEC_DATA_IN_FILE_FOR_DATE, oe.getSpecifier());
        }
    }

    @Test
    public void testLaterDate() {
        final double latitude  = FastMath.toRadians(60.0);
        final double longitude = FastMath.toRadians(-130.0);
        final GeodeticPoint point = new GeodeticPoint(latitude, longitude, 0.0);

        try {
            model.pathDelay(state, new TopocentricFrame(earth, point, null),
                            Frequency.G01.getMHzFrequency() * 1.0e6,
                            model.getParameters(new AbsoluteDate()));
            Assertions.fail("An exception should have been thrown");
        } catch (OrekitException oe) {
            Assertions.assertEquals(OrekitMessages.NO_TEC_DATA_IN_FILE_FOR_DATE, oe.getSpecifier());
        }

    }

    @Test
    public void testFieldLaterDate() {
        doTestFieldLaterDate(Binary64Field.getInstance());
    }

    private <T extends CalculusFieldElement<T>> void doTestFieldLaterDate(final Field<T> field) {
        final double latitude  = FastMath.toRadians(60.0);
        final double longitude = FastMath.toRadians(-130.0);
        final GeodeticPoint point = new GeodeticPoint(latitude, longitude, 0.0);
        try {
            model.pathDelay(new FieldSpacecraftState<>(field, state), new TopocentricFrame(earth, point, null),
                            Frequency.G01.getMHzFrequency() * 1.0e6,
                            model.getParameters(field, new FieldAbsoluteDate<>(field)));
            Assertions.fail("An exception should have been thrown");
        } catch (OrekitException oe) {
            Assertions.assertEquals(OrekitMessages.NO_TEC_DATA_IN_FILE_FOR_DATE, oe.getSpecifier());
        }

    }

    @Test
    /**
     * The goal of this test is to verify if an OrekitException is thrown when latitude or longitude
     * bondaries are not present in the header section of the Global Ionosphere Map.
     */
    public void testIssue621() {
        final String fileName  = "missing-lat-lon-header-gpsg0150.19i";
        final double latitude  = FastMath.toRadians(30.0);
        final double longitude = FastMath.toRadians(-130.0);

        try {
            GlobalIonosphereMapModel corruptedModel = new GlobalIonosphereMapModel(fileName);
            corruptedModel.getTEC(new AbsoluteDate(2019, 1, 15, 0, 0, 0.0, TimeScalesFactory.getUTC()),
                         new GeodeticPoint(latitude, longitude, 0.0));
            Assertions.fail("An exception should have been thrown");

        } catch (OrekitException oe) {
            Assertions.assertEquals(OrekitMessages.NO_LATITUDE_LONGITUDE_BONDARIES_IN_IONEX_HEADER, oe.getSpecifier());
        }
    }

    @Test
    public void testMissingEpochInHeader() {
        final String fileName  = "missing-epoch-header-gpsg0150.19i";
        final double latitude  = FastMath.toRadians(30.0);
        final double longitude = FastMath.toRadians(-130.0);

        try {
            GlobalIonosphereMapModel corruptedModel = new GlobalIonosphereMapModel(fileName);
            corruptedModel.getTEC(new AbsoluteDate(2019, 1, 15, 0, 0, 0.0, TimeScalesFactory.getUTC()),
                         new GeodeticPoint(latitude, longitude, 0.0));
            Assertions.fail("An exception should have been thrown");

        } catch (OrekitException oe) {
            Assertions.assertEquals(OrekitMessages.NO_EPOCH_IN_IONEX_HEADER, oe.getSpecifier());
        }

    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        GlobalIonosphereMapModel original = new GlobalIonosphereMapModel("gpsg0150.19i");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream    oos = new ObjectOutputStream(bos);
        oos.writeObject(original);

        Assertions.assertTrue(bos.size() > 150);
        Assertions.assertTrue(bos.size() < 200);

        ByteArrayInputStream  bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream     ois = new ObjectInputStream(bis);
        GlobalIonosphereMapModel deserialized = (GlobalIonosphereMapModel) ois.readObject();

        AbsoluteDate date = new AbsoluteDate(2019, 1, 15, 3, 43, 12.0, TimeScalesFactory.getUTC());
        GeodeticPoint point = new GeodeticPoint(FastMath.toRadians(30.0), FastMath.toRadians(-130.0), 0.0);
        Assertions.assertEquals(original.getTEC(date, point), deserialized.getTEC(date, point), 1.0e-12);
    }

}
