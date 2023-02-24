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

package org.orekit.frames;

import org.hipparchus.CalculusFieldElement;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FieldAbsoluteDate;

public class PythonEOPBasedTransformProvider implements EOPBasedTransformProvider {

    private static final long serialVersionUID = 6434996513250596254L;
    
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
     * Get the EOP history.
     *
     * @return EOP history
     */
    @Override
    public native EOPHistory getEOPHistory();

    /**
     * Get a version of the provider that does <em>not</em> cache tidal corrections.
     * <p>
     * This method removes the performance enhancing interpolation features that are
     * used by default in EOP-based provider, in order to focus on accuracy. The
     * interpolation features are intended to save processing time by avoiding doing
     * tidal correction evaluation at each time step and caching some results. This
     * method can be used to avoid this (it is automatically called by {@link
     * FramesFactory#getNonInterpolatingTransform(Frame, Frame, AbsoluteDate)}, when
     * very high accuracy is desired, or for testing purposes. It should be used with
     * care, as doing the full computation is <em>really</em> costly.
     * </p>
     *
     * @return version of the provider that does <em>not</em> cache tidal corrections
     * @see FramesFactory#getNonInterpolatingTransform(Frame, Frame, AbsoluteDate)
     */
    @Override
    public native EOPBasedTransformProvider getNonInterpolatingProvider();

    /**
     * Get the {@link Transform} corresponding to specified date.
     *
     * @param date current date
     * @return transform at specified date
     */
    @Override
    public native Transform getTransform(AbsoluteDate date);

    /**
     * Get the {@link FieldTransform} corresponding to specified date.
     *
     * @param date current date
     * @return transform at specified date
     * @since 9.0
     */
    @Override
    public <T extends CalculusFieldElement<T>> FieldTransform<T> getTransform(FieldAbsoluteDate<T> date) {
        return this.getTransform_F(date);
    }

    /**
     * Get the {@link FieldTransform} corresponding to specified date.
     *
     * @param date current date
     * @return transform at specified date
     * @since 9.0
     */
    public native <T extends CalculusFieldElement<T>> FieldTransform<T> getTransform_F(FieldAbsoluteDate<T> date);
}
