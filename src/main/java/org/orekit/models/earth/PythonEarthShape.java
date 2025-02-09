/* Contributed in the public domain.
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

package org.orekit.models.earth;

import org.hipparchus.CalculusFieldElement;
import org.hipparchus.geometry.euclidean.threed.FieldLine;
import org.hipparchus.geometry.euclidean.threed.FieldVector3D;
import org.hipparchus.geometry.euclidean.threed.Line;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.FieldGeodeticPoint;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.FieldAbsoluteDate;
import org.orekit.utils.TimeStampedPVCoordinates;

public class PythonEarthShape implements EarthShape {

    private static final long serialVersionUID = -3177940928549930595L;
    
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
    public native ReferenceEllipsoid getEllipsoid();

    /** {@inheritDoc} */
    @Override
    public native Frame getBodyFrame();

    @Override
    public native GeodeticPoint getIntersectionPoint(Line line, Vector3D close, Frame frame, AbsoluteDate date);

    @Override
    public native <T extends CalculusFieldElement<T>> FieldGeodeticPoint<T> getIntersectionPoint(FieldLine<T> line, FieldVector3D<T> close, Frame frame, FieldAbsoluteDate<T> date);

    /** {@inheritDoc} */
    @Override
    public native Vector3D projectToGround(Vector3D point, AbsoluteDate date, Frame frame);

    /** {@inheritDoc} */
    @Override
    public native TimeStampedPVCoordinates projectToGround(TimeStampedPVCoordinates pv, Frame frame);

    /** {@inheritDoc} */
    @Override
    public native GeodeticPoint transform(Vector3D point, Frame frame, AbsoluteDate date);

    /** {@inheritDoc} */
    @Override
    public native <T extends CalculusFieldElement<T>> FieldGeodeticPoint<T> transform(FieldVector3D<T> point, Frame frame, FieldAbsoluteDate<T> date);

    /** {@inheritDoc} */
    @Override
    public native Vector3D transform(GeodeticPoint point);

    /** {@inheritDoc} */
    @Override
    public native <T extends CalculusFieldElement<T>> FieldVector3D<T> transform(FieldGeodeticPoint<T> point);

}
