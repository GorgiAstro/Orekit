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


package org.orekit.propagation.semianalytical.dsst.forces;

import org.orekit.orbits.Orbit;
import org.orekit.time.AbsoluteDate;

import java.util.Map;
import java.util.Set;

public class PythonShortPeriodTerms implements ShortPeriodTerms {

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
     * Evaluate the contributions of the short period terms.
     *
     * @param meanOrbit mean orbit to which the short period contribution applies
     * @return short period terms contributions
     */
    @Override
    public native double[] value(Orbit meanOrbit);

    /**
     * Get the prefix for short period coefficients keys.
     * <p>
     * This prefix is used to identify the coefficients of the
     * current force model from the coefficients pertaining to
     * other force models. All the keys in the map returned by
     * {@link #getCoefficients(AbsoluteDate, Set)}
     * start with this prefix, which must be unique among all
     * providers.
     * </p>
     *
     * @return the prefix for short periodic coefficients keys
     * @see #getCoefficients(AbsoluteDate, Set)
     */
    @Override
    public native String getCoefficientsKeyPrefix();

    /**
     * Computes the coefficients involved in the contributions.
     * <p>
     * This method is intended mainly for validation purposes. Its output
     * is highly dependent on the implementation details in each force model
     * and may change from version to version. It is <em>not</em> recommended
     * to use it for any operational purposes.
     * </p>
     *
     * @param date     current date
     * @param selected set of coefficients that should be put in the map
     *                 (empty set means all coefficients are selected)
     * @return the selected coefficients of the short periodic variations,
     * in a map where all keys start with {@link #getCoefficientsKeyPrefix()}
     */
    @Override
    public native Map<String, double[]> getCoefficients(AbsoluteDate date, Set<String> selected);
}
