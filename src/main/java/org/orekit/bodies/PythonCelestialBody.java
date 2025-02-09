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

package org.orekit.bodies;

import org.hipparchus.Field;
import org.hipparchus.CalculusFieldElement;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FieldAbsoluteDate;
import org.orekit.utils.*;

public class PythonCelestialBody implements CelestialBody {

    private static final long serialVersionUID = -7481310063914250761L;
    
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

    /** {@inheritDoc} */
    @Override
    public native Frame getInertiallyOrientedFrame();

    /** {@inheritDoc} */
    @Override
    public native Frame getBodyOrientedFrame();

    /** {@inheritDoc} */
    @Override
    public native String getName();

    /** {@inheritDoc} */
    @Override
    public native double getGM();

    /** {@inheritDoc} */
    @Override
    public native <T extends CalculusFieldElement<T>> FieldPVCoordinatesProvider<T> toFieldPVCoordinatesProvider(Field<T> field);

    /** {@inheritDoc} */
    @Override
    public native <T extends CalculusFieldElement<T>> TimeStampedFieldPVCoordinates<T> getPVCoordinates(FieldAbsoluteDate<T> date, Frame frame);

    /** {@inheritDoc} */
    @Override
    public native TimeStampedPVCoordinates getPVCoordinates(AbsoluteDate date, Frame frame);
}
