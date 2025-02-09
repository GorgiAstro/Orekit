/** Copyright 2014 SSC and 2002-2014 CS Systèmes d'Information
 * Licensed to CS SystÃ¨mes d'Information (CS) under one or more
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

// this file was created by SSC and is largely a derived work from the
// original java class created by CS Systèmes d'Information

package org.orekit.propagation.sampling;

import org.orekit.propagation.SpacecraftState;
 ;
import org.orekit.time.AbsoluteDate;

/** This interface is a space-dynamics aware fixed size step handler.
 *
 * <p>It mirrors the <code>FixedStepHandler</code> interface from <a
 * href="http://commons.apache.org/math/">commons-math</a> but provides
 * a space-dynamics interface to the methods.</p>
 * @author Luc Maisonobe
 */

public class PythonOrekitFixedStepHandler implements OrekitFixedStepHandler {

    /** Part of JCC Python interface to object */
    protected long pythonObject;
    public void pythonExtension(long pythonObject) {
        this.pythonObject = pythonObject;
    }
    public long pythonExtension() {
        return this.pythonObject;
    }
    public void finalize() throws Throwable { pythonDecRef(); }
    public native void pythonDecRef();

    /** {@inheritDoc} */
    @Override
    public native void init(SpacecraftState s0, AbsoluteDate t, double step);

    /** {@inheritDoc} */
    @Override
    public native void handleStep(SpacecraftState currentState);

    /** {@inheritDoc} */
    @Override
    public native void finish(SpacecraftState finalState);
}
