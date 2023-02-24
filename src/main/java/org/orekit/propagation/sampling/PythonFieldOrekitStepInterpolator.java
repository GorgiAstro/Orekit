/* Copyright 2002-2019 CS Systèmes d'Information
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
// this file was created by SCC 2019 and is largely a derived work from the
// original java class/interface

package org.orekit.propagation.sampling;

import org.hipparchus.CalculusFieldElement;
import org.orekit.propagation.FieldSpacecraftState;
import org.orekit.time.FieldAbsoluteDate;

public class PythonFieldOrekitStepInterpolator<T extends CalculusFieldElement<T>> implements FieldOrekitStepInterpolator<T> {
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
     * Get the state at previous grid point date.
     *
     * @return state at previous grid point date
     */
    @Override
    public native FieldSpacecraftState<T> getPreviousState();

    /**
     * Get the state at previous grid point date.
     *
     * @return state at previous grid point date
     */
    @Override
    public native FieldSpacecraftState<T> getCurrentState();

    /**
     * Get the state at interpolated date.
     *
     * @param date date of the interpolated state
     * @return state at interpolated date
     * the date
     */
    @Override
    public native FieldSpacecraftState<T> getInterpolatedState(FieldAbsoluteDate<T> date);

    /**
     * Check is integration direction is forward in date.
     *
     * @return true if integration is forward in date
     */
    @Override
    public native boolean isForward();

    /**
     * Create a new restricted version of the instance.
     * <p>
     * The instance is not changed at all.
     * </p>
     *
     * @param newPreviousState start of the restricted step
     * @param newCurrentState  end of the restricted step
     * @return restricted version of the instance
     * @see #getPreviousState()
     * @see #getCurrentState()
     * @since 11.0
     */
    @Override
    public native FieldOrekitStepInterpolator<T> restrictStep(FieldSpacecraftState<T> newPreviousState, FieldSpacecraftState<T> newCurrentState);
}
