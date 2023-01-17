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

package org.orekit.forces.drag;

import org.hipparchus.CalculusFieldElement;
import org.hipparchus.geometry.euclidean.threed.FieldRotation;
import org.hipparchus.geometry.euclidean.threed.FieldVector3D;
import org.hipparchus.geometry.euclidean.threed.Rotation;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FieldAbsoluteDate;
import org.orekit.utils.ParameterDriver;

import java.util.List;

public class PythonDragSensitive implements DragSensitive {

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
     * Get the drivers for supported parameters.
     * Extension point for Python.
     *
     * @return parameters drivers
     * @since 8.0
     */
    @Override
    public native List<ParameterDriver> getDragParametersDrivers();

    /**
     * Compute the acceleration due to drag.
     * Extension point for Python.
     *
     * <p>
     * The computation includes all spacecraft specific characteristics
     * like shape, area and coefficients.
     * </p>
     *
     * @param date             current date
     * @param frame            inertial reference frame for state (both orbit and attitude)
     * @param position         position of spacecraft in reference frame
     * @param rotation         orientation (attitude) of the spacecraft with respect to reference frame
     * @param mass             current mass
     * @param density          atmospheric density at spacecraft position
     * @param relativeVelocity relative velocity of atmosphere with respect to spacecraft,
     *                         in the same inertial frame as spacecraft orbit (m/s)
     * @param parameters       values of the force model parameters
     * @return spacecraft acceleration in the same inertial frame as spacecraft orbit (m/s²)
     */
    @Override
    public native Vector3D dragAcceleration(AbsoluteDate date, Frame frame, Vector3D position, Rotation rotation, double mass, double density, Vector3D relativeVelocity, double[] parameters);

    /**
     * Compute the acceleration due to drag.
     * Connects to dragAcceleration_FFFFTTFT for Python extension.
     * <p>
     * The computation includes all spacecraft specific characteristics
     * like shape, area and coefficients.
     * </p>
     *
     * @param date             current date
     * @param frame            inertial reference frame for state (both orbit and attitude)
     * @param position         position of spacecraft in reference frame
     * @param rotation         orientation (attitude) of the spacecraft with respect to reference frame
     * @param mass             current mass
     * @param density          atmospheric density at spacecraft position
     * @param relativeVelocity relative velocity of atmosphere with respect to spacecraft,
     *                         in the same inertial frame as spacecraft orbit (m/s)
     * @param parameters       values of the force model parameters
     * @return spacecraft acceleration in the same inertial frame as spacecraft orbit (m/s²)
     * @since 9.0
     */
    @Override
    public <T extends CalculusFieldElement<T>> FieldVector3D<T> dragAcceleration(FieldAbsoluteDate<T> date, Frame frame, FieldVector3D<T> position, FieldRotation<T> rotation, T mass, T density, FieldVector3D<T> relativeVelocity, T[] parameters) {
        return this.dragAcceleration_FFFFTTFT(date,frame, position, rotation, mass, density, relativeVelocity, parameters);
    }

    /* Extension point for Python. Connected to dragAcceleration(...)*/
    public native <T extends CalculusFieldElement<T>> FieldVector3D<T> dragAcceleration_FFFFTTFT(FieldAbsoluteDate<T> date, Frame frame, FieldVector3D<T> position, FieldRotation<T> rotation, T mass, T density, FieldVector3D<T> relativeVelocity, T[] parameters);


}
