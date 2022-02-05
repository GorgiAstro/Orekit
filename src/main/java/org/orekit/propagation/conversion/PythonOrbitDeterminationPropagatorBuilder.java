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

// this file was created by SSC 2021 and is largely a derived work from the
// original java class

package org.orekit.propagation.conversion;

import org.orekit.estimation.leastsquares.AbstractBatchLSModel;
import org.orekit.estimation.leastsquares.ModelObserver;
import org.orekit.estimation.measurements.ObservedMeasurement;
import org.orekit.estimation.sequential.AbstractKalmanModel;
import org.orekit.estimation.sequential.CovarianceMatrixProvider;
import org.orekit.frames.Frame;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.Propagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.ParameterDriversList;

import java.util.List;

public class PythonOrbitDeterminationPropagatorBuilder implements OrbitDeterminationPropagatorBuilder {

    /** Part of JCC Python interface to object */
    private long pythonObject;

    /** Part of JCC Python interface to object */
    public void pythonExtension(long pythonObject)
    {
        this.pythonObject = pythonObject;
    }

    /** Part of JCC Python interface to object */
    public long pythonExtension()
    {
        return this.pythonObject;
    }

    /** Part of JCC Python interface to object */
    public void finalize()
            throws Throwable
    {
        pythonDecRef();
    }

    /** Part of JCC Python interface to object */
    public native void pythonDecRef();

    /**
     * Build a new batch least squares model.
     *
     * @param builders                        builders to use for propagation
     * @param measurements                    measurements
     * @param estimatedMeasurementsParameters estimated measurements parameters
     * @param observer                        observer to be notified at model calls
     * @return a new model for the Batch Least Squares orbit determination
     */
    @Override
    public native AbstractBatchLSModel buildLSModel(OrbitDeterminationPropagatorBuilder[] builders, List<ObservedMeasurement<?>> measurements, ParameterDriversList estimatedMeasurementsParameters, ModelObserver observer);

    /**
     * Build a new Kalman model.
     *
     * @param propagatorBuilders              propagators builders used to evaluate the orbits.
     * @param covarianceMatricesProviders     providers for covariance matrices
     * @param estimatedMeasurementsParameters measurement parameters to estimate
     * @param measurementProcessNoiseMatrix   provider for measurement process noise matrix
     * @return a new model for Kalman Filter orbit determination
     */
    @Override
    public native AbstractKalmanModel buildKalmanModel(List<OrbitDeterminationPropagatorBuilder> propagatorBuilders, List<CovarianceMatrixProvider> covarianceMatricesProviders, ParameterDriversList estimatedMeasurementsParameters, CovarianceMatrixProvider measurementProcessNoiseMatrix);

    /**
     * Reset the orbit in the propagator builder.
     *
     * @param newOrbit New orbit to set in the propagator builder
     */
    @Override
    public native void resetOrbit(Orbit newOrbit);

    /**
     * Build a propagator.
     *
     * @param normalizedParameters normalized values for the selected parameters
     * @return an initialized propagator
     */
    @Override
    public native Propagator buildPropagator(double[] normalizedParameters);

    /**
     * Get the current value of selected normalized parameters.
     *
     * @return current value of selected normalized parameters
     */
    @Override
    public native double[] getSelectedNormalizedParameters();

    /**
     * Get the orbit type expected for the 6 first parameters in
     * {@link #buildPropagator(double[])}.
     *
     * @return orbit type to use in {@link #buildPropagator(double[])}
     * @see #buildPropagator(double[])
     * @see #getPositionAngle()
     * @since 7.1
     */
    @Override
    public native OrbitType getOrbitType();

    /**
     * Get the position angle type expected for the 6 first parameters in
     * {@link #buildPropagator(double[])}.
     *
     * @return position angle type to use in {@link #buildPropagator(double[])}
     * @see #buildPropagator(double[])
     * @see #getOrbitType()
     * @since 7.1
     */
    @Override
    public native PositionAngle getPositionAngle();

    /**
     * Get the date of the initial orbit.
     *
     * @return date of the initial orbit
     */
    @Override
    public native AbsoluteDate getInitialOrbitDate();

    /**
     * Get the frame in which the orbit is propagated.
     *
     * @return frame in which the orbit is propagated
     */
    @Override
    public native Frame getFrame();

    /**
     * Get the drivers for the configurable orbital parameters.
     *
     * @return drivers for the configurable orbital parameters
     * @since 8.0
     */
    @Override
    public native ParameterDriversList getOrbitalParametersDrivers();

    /**
     * Get the drivers for the configurable propagation parameters.
     * <p>
     * The parameters typically correspond to force models.
     * </p>
     *
     * @return drivers for the configurable propagation parameters
     * @since 8.0
     */
    @Override
    public native ParameterDriversList getPropagationParametersDrivers();
}