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
//
//

package org.orekit.forces.gravity.potential;

import org.orekit.time.AbsoluteDate;

public class PythonUnnormalizedSphericalHarmonics implements UnnormalizedSphericalHarmonicsProvider.UnnormalizedSphericalHarmonics {



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
     * Get a spherical harmonic cosine coefficient.
     *
     * @param n degree of the coefficient
     * @param m order of the coefficient
     * @return un-normalized coefficient Cnm
     */
    @Override
    public native double getUnnormalizedCnm(int n, int m);

    /**
     * Get a spherical harmonic sine coefficient.
     *
     * @param n degree of the coefficient
     * @param m order of the coefficient
     * @return un-normalized coefficient Snm
     */
    @Override
    public native double getUnnormalizedSnm(int n, int m);

    /**
     * Get the date.
     *
     * @return date attached to the object
     */
    @Override
    public native AbsoluteDate getDate();
}
