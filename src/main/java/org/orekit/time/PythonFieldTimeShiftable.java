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

package org.orekit.time;

import org.hipparchus.CalculusFieldElement;

public class PythonFieldTimeShiftable<T extends FieldTimeShiftable<T, KK>, KK extends CalculusFieldElement<KK>> implements FieldTimeShiftable<T, KK>  {
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
     * Get a time-shifted instance.
     *
     * @param dt time shift in seconds
     * @return a new instance, shifted with respect to instance (which is not changed)
     */
    @Override
    public native T shiftedBy(double dt);

    /**
     * Get a time-shifted instance. Calls the ShiftedByType Python extension method
     *
     * @param dt time shift in seconds
     * @return a new instance, shifted with respect to instance (which is not changed)
     */
    @Override
    public T shiftedBy(KK dt) {
        return this.shiftedBy_KK(dt);
    }


    /**
     * Get a time-shifted instance. The Python extension method.
     *
     * @param dt time shift in seconds
     * @return a new instance, shifted with respect to instance (which is not changed)
     */
    public native T shiftedBy_KK(KK dt);

}
