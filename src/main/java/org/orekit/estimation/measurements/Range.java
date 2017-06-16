/* Copyright 2002-2017 CS Systèmes d'Information
 * Licensed to CS Systèmes d'Information (CS) under one or more
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
package org.orekit.estimation.measurements;

import java.util.Arrays;

import org.hipparchus.Field;
import org.hipparchus.analysis.differentiation.DSFactory;
import org.hipparchus.analysis.differentiation.DerivativeStructure;
import org.hipparchus.geometry.euclidean.threed.FieldVector3D;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.errors.OrekitException;
import org.orekit.frames.FieldTransform;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FieldAbsoluteDate;
import org.orekit.utils.Constants;
import org.orekit.utils.TimeStampedFieldPVCoordinates;

/** Class modeling a range measurement from a ground station.
 * <p>
 * The measurement is considered to be a signal emitted from
 * a ground station, reflected on spacecraft, and received
 * on the same ground station. Its value is the elapsed time
 * between emission and reception divided by 2c were c is the
 * speed of light. The motion of both the station and the
 * spacecraft during the signal flight time are taken into
 * account. The date of the measurement corresponds to the
 * reception on ground of the reflected signal.
 * </p>
 * @author Thierry Ceolin
 * @author Luc Maisonobe
 * @author Maxime Journot
 * @since 8.0
 */
public class Range extends AbstractMeasurement<Range> {

    /** Ground station from which measurement is performed. */
    private final GroundStation station;

    /** Simple constructor.
     * @param station ground station from which measurement is performed
     * @param date date of the measurement
     * @param range observed value
     * @param sigma theoretical standard deviation
     * @param baseWeight base weight
     * @exception OrekitException if a {@link org.orekit.utils.ParameterDriver}
     * name conflict occurs
     */
    public Range(final GroundStation station, final AbsoluteDate date,
                 final double range, final double sigma, final double baseWeight)
        throws OrekitException {
        super(date, range, sigma, baseWeight,
              station.getEastOffsetDriver(),
              station.getNorthOffsetDriver(),
              station.getZenithOffsetDriver(),
              station.getPrimeMeridianOffsetDriver(),
              station.getPrimeMeridianDriftDriver(),
              station.getPolarOffsetXDriver(),
              station.getPolarDriftXDriver(),
              station.getPolarOffsetYDriver(),
              station.getPolarDriftYDriver());
        this.station = station;
    }

    /** Get the ground station from which measurement is performed.
     * @return ground station from which measurement is performed
     */
    public GroundStation getStation() {
        return station;
    }

    /** {@inheritDoc} */
    @Override
    protected EstimatedMeasurement<Range> theoreticalEvaluation(final int iteration,
                                                                final int evaluation,
                                                                final SpacecraftState state)
        throws OrekitException {

        // Range derivatives are computed with respect to spacecraft state in inertial frame
        // and station position in station's offset frame
        // -------
        //
        // Parameters:
        //  - 0..2 - Px, Py, Pz   : Position of the spacecraft in inertial frame
        //  - 3..5 - Vx, Vy, Vz   : Velocity of the spacecraft in inertial frame
        //  - 6..8 - QTx, QTy, QTz: Position of the station in station's offset frame
        int nbParams = 6;
        final int primeMeridianOffsetIndex;
        if (station.getPrimeMeridianOffsetDriver().isSelected()) {
            primeMeridianOffsetIndex = nbParams++;
        } else {
            primeMeridianOffsetIndex = -1;
        }
        final int primeMeridianDriftIndex;
        if (station.getPrimeMeridianDriftDriver().isSelected()) {
            primeMeridianDriftIndex = nbParams++;
        } else {
            primeMeridianDriftIndex = -1;
        }
        final int polarOffsetXIndex;
        if (station.getPolarOffsetXDriver().isSelected()) {
            polarOffsetXIndex = nbParams++;
        } else {
            polarOffsetXIndex = -1;
        }
        final int polarDriftXIndex;
        if (station.getPolarDriftXDriver().isSelected()) {
            polarDriftXIndex = nbParams++;
        } else {
            polarDriftXIndex = -1;
        }
        final int polarOffsetYIndex;
        if (station.getPolarOffsetYDriver().isSelected()) {
            polarOffsetYIndex = nbParams++;
        } else {
            polarOffsetYIndex = -1;
        }
        final int polarDriftYIndex;
        if (station.getPolarDriftYDriver().isSelected()) {
            polarDriftYIndex = nbParams++;
        } else {
            polarDriftYIndex = -1;
        }
        final int eastOffsetIndex;
        if (station.getEastOffsetDriver().isSelected()) {
            eastOffsetIndex = nbParams++;
        } else {
            eastOffsetIndex = -1;
        }
        final int northOffsetIndex;
        if (station.getNorthOffsetDriver().isSelected()) {
            northOffsetIndex = nbParams++;
        } else {
            northOffsetIndex = -1;
        }
        final int zenithOffsetIndex;
        if (station.getZenithOffsetDriver().isSelected()) {
            zenithOffsetIndex = nbParams++;
        } else {
            zenithOffsetIndex = -1;
        }
        final DSFactory                          factory = new DSFactory(nbParams, 1);
        final Field<DerivativeStructure>         field   = factory.getDerivativeField();
        final FieldVector3D<DerivativeStructure> zero    = FieldVector3D.getZero(field);

        // Position of the spacecraft expressed as a derivative structure
        // The components of the position are the 3 first derivative parameters
        final Vector3D stateP = state.getPVCoordinates().getPosition();
        final FieldVector3D<DerivativeStructure> pDS =
                        new FieldVector3D<>(factory.variable(0, stateP.getX()),
                                            factory.variable(1, stateP.getY()),
                                            factory.variable(2, stateP.getZ()));

        // Velocity of the spacecraft expressed as a derivative structure
        // The components of the velocity are the 3 second derivative parameters
        final Vector3D stateV = state.getPVCoordinates().getVelocity();
        final FieldVector3D<DerivativeStructure> vDS =
                        new FieldVector3D<>(factory.variable(3, stateV.getX()),
                                            factory.variable(4, stateV.getY()),
                                            factory.variable(5, stateV.getZ()));

        // Acceleration of the spacecraft
        // The components of the acceleration are not derivative parameters
        final Vector3D stateA = state.getPVCoordinates().getAcceleration();
        final FieldVector3D<DerivativeStructure> aDS =
                        new FieldVector3D<>(factory.constant(stateA.getX()),
                                            factory.constant(stateA.getY()),
                                            factory.constant(stateA.getZ()));

        final TimeStampedFieldPVCoordinates<DerivativeStructure> pvaDS =
                        new TimeStampedFieldPVCoordinates<>(state.getDate(), pDS, vDS, aDS);

        // transform between station and inertial frame, expressed as a derivative structure
        // The components of station's position in offset frame are the 3 last derivative parameters
        final AbsoluteDate downlinkDate = getDate();
        final FieldAbsoluteDate<DerivativeStructure> downlinkDateDS =
                        new FieldAbsoluteDate<>(field, downlinkDate);
        final FieldTransform<DerivativeStructure> offsetToInertialDownlink =
                        station.getOffsetToInertial(state.getFrame(), downlinkDateDS, factory,
                                                    primeMeridianOffsetIndex, primeMeridianDriftIndex,
                                                    polarOffsetXIndex, polarDriftXIndex,
                                                    polarOffsetYIndex, polarDriftYIndex,
                                                    eastOffsetIndex, northOffsetIndex, zenithOffsetIndex);

        // Station position in inertial frame at end of the downlink leg
        final TimeStampedFieldPVCoordinates<DerivativeStructure> stationDownlink =
                        offsetToInertialDownlink.transformPVCoordinates(new TimeStampedFieldPVCoordinates<>(downlinkDateDS,
                                                                                                            zero, zero, zero));

        // Compute propagation times
        // (if state has already been set up to pre-compensate propagation delay,
        //  we will have delta == tauD and transitState will be the same as state)

        // Downlink delay
        final DerivativeStructure tauD = station.signalTimeOfFlight(pvaDS, stationDownlink.getPosition(), downlinkDateDS);

        // Transit state
        final double                delta        = downlinkDate.durationFrom(state.getDate());
        final DerivativeStructure   deltaMTauD   = tauD.negate().add(delta);
        final SpacecraftState       transitState = state.shiftedBy(deltaMTauD.getValue());

        // Transit state (re)computed with derivative structures
        final TimeStampedFieldPVCoordinates<DerivativeStructure> transitStateDS = pvaDS.shiftedBy(deltaMTauD);

        // Station at transit state date (derivatives of tauD taken into account)
        final TimeStampedFieldPVCoordinates<DerivativeStructure> stationAtTransitDate =
                        stationDownlink.shiftedBy(tauD.negate());

        // Uplink delay
        final DerivativeStructure tauU = station.signalTimeOfFlight(stationAtTransitDate,
                                                                    transitStateDS.getPosition(),
                                                                    transitStateDS.getDate());
        // Prepare the evaluation
        final EstimatedMeasurement<Range> estimated =
                        new EstimatedMeasurement<Range>(this, iteration, evaluation, transitState);

        // Range value
        final double              cOver2 = 0.5 * Constants.SPEED_OF_LIGHT;
        final DerivativeStructure tau    = tauD.add(tauU);
        final DerivativeStructure range  = tau.multiply(cOver2);
        estimated.setEstimatedValue(range.getValue());

        // Range partial derivatives with respect to state
        final double[] derivatives = range.getAllDerivatives();
        estimated.setStateDerivatives(Arrays.copyOfRange(derivatives, 1, 7));

        // Set parameter drivers partial derivatives with respect to station position in offset topocentric frame
        if (eastOffsetIndex >= 0) {
            estimated.setParameterDerivatives(station.getEastOffsetDriver(), derivatives[eastOffsetIndex + 1]);
        }
        if (northOffsetIndex >= 0) {
            estimated.setParameterDerivatives(station.getNorthOffsetDriver(), derivatives[northOffsetIndex + 1]);
        }
        if (zenithOffsetIndex >= 0) {
            estimated.setParameterDerivatives(station.getZenithOffsetDriver(), derivatives[zenithOffsetIndex + 1]);
        }

        return estimated;

    }

}
