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

package org.orekit.attitudes;


import org.hipparchus.CalculusFieldElement;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FieldAbsoluteDate;
import org.orekit.utils.FieldPVCoordinatesProvider;
import org.orekit.utils.PVCoordinatesProvider;

public class PythonAttitudeProviderModifier  implements AttitudeProviderModifier {

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
     * Get the underlying attitude provider.
     * Extension point for Python.
     *
     * @return underlying attitude provider
     */
    @Override
    public native AttitudeProvider getUnderlyingAttitudeProvider();

    /**
     * Compute the attitude corresponding to an orbital state.
     * Extension point for Python.
     *
     * @param pvProv local position-velocity provider around current date
     * @param date   current date
     * @param frame  reference frame from which attitude is computed
     * @return attitude attitude on the specified date and position-velocity state
     */
    @Override
    public native Attitude getAttitude(PVCoordinatesProvider pvProv, AbsoluteDate date, Frame frame);

    /**
     * Compute the attitude corresponding to an orbital state.
     * Redirects to getAttitude_FFF(...) for Python extension
     *
     * @param pvProv local position-velocity provider around current date
     * @param date   current date
     * @param frame  reference frame from which attitude is computed
     * @return attitude attitude on the specified date and position-velocity state
     * @since 9.0
     */
    @Override
    public <T extends CalculusFieldElement<T>> FieldAttitude<T> getAttitude(FieldPVCoordinatesProvider<T> pvProv, FieldAbsoluteDate<T> date, Frame frame) {
        return this.getAttitude_FFF(pvProv, date, frame);
    }

    /**
     * Compute the attitude corresponding to an orbital state.
     * Extension point for Python. Connected to getAttitude(...)
     *
     * @param pvProv local position-velocity provider around current date
     * @param date   current date
     * @param frame  reference frame from which attitude is computed
     * @return attitude attitude on the specified date and position-velocity state
     * @since 9.3
     */
    public native <T extends CalculusFieldElement<T>> FieldAttitude<T> getAttitude_FFF(FieldPVCoordinatesProvider<T> pvProv, FieldAbsoluteDate<T> date, Frame frame);
}
