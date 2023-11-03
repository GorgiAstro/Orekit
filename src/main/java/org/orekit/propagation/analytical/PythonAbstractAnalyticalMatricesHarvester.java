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

// this file was created by SSC 2022 and is largely a derived work from the
// original java class/interface

package org.orekit.propagation.analytical;

import org.hipparchus.linear.RealMatrix;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngleType;
import org.orekit.utils.DoubleArrayDictionary;

public class PythonAbstractAnalyticalMatricesHarvester extends AbstractAnalyticalMatricesHarvester {

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
     * Simple constructor.
     * <p>
     * The arguments for initial matrices <em>must</em> be compatible with the
     * {@link OrbitType orbit type}
     * and {@link PositionAngleType position angle} that will be used by propagator
     * </p>
     *
     * @param propagator             propagator bound to this harvester
     * @param stmName                State Transition Matrix state name
     * @param initialStm             initial State Transition Matrix ∂Y/∂Y₀,
     *                               if null (which is the most frequent case), assumed to be 6x6 identity
     * @param initialJacobianColumns initial columns of the Jacobians matrix with respect to parameters,
     *                               if null or if some selected parameters are missing from the dictionary, the corresponding
     */
    public PythonAbstractAnalyticalMatricesHarvester(AbstractAnalyticalPropagator propagator, String stmName, RealMatrix initialStm, DoubleArrayDictionary initialJacobianColumns) {
        super(propagator, stmName, initialStm, initialJacobianColumns);
    }

    /** {@inheritDoc} */
    @Override
    public native AbstractAnalyticalGradientConverter getGradientConverter();
}
