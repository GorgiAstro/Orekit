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

package org.orekit.python;

import org.hipparchus.CalculusFieldElement;
import org.orekit.propagation.FieldSpacecraftState;
import org.orekit.propagation.sampling.FieldOrekitFixedStepHandler;
import org.orekit.time.FieldAbsoluteDate;

public class PythonFieldOrekitFixedStepHandler<T extends CalculusFieldElement<T>> implements FieldOrekitFixedStepHandler<T> {

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
     * Initialize step handler at the start of a propagation.
     * <p>
     * This method is called once at the start of the propagation. It
     * may be used by the step handler to initialize some internal data
     * if needed.
     * </p>
     *
     * @param s0   initial state
     * @param t    target time for the integration
     * @param step the duration in seconds of the fixed step. This value is
     */
    @Override
    public native void init(FieldSpacecraftState<T> s0, FieldAbsoluteDate<T> t, T step);

    /**
     * Handle the current step.
     *
     * @param currentState current state at step time
     * @param isLast       if true, this is the last integration step
     */
    @Override
    public native void handleStep(FieldSpacecraftState<T> currentState, boolean isLast);
}
