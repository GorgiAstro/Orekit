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

package org.orekit.forces.gravity.potential;

import org.orekit.python.JCCBase;
import org.orekit.time.AbsoluteDate;

public class PythonRawSphericalHarmonicsProvider implements RawSphericalHarmonicsProvider {

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
    public native RawSphericalHarmonics onDate(AbsoluteDate date);

    /** {@inheritDoc} */
    @Override
    public native int getMaxDegree();

    /** {@inheritDoc} */
    @Override
    public native int getMaxOrder();

    /** {@inheritDoc} */
    @Override
    public native double getMu();

    /** {@inheritDoc} */
    @Override
    public native double getAe();

    /** {@inheritDoc} */
    @Override
    public native AbsoluteDate getReferenceDate();

    /** {@inheritDoc} */
    @Override
    public native TideSystem getTideSystem();
}
