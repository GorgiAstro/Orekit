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

package org.orekit.python;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.models.earth.tessellation.TileAiming;

public class PythonTileAiming implements TileAiming {

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
     * Find the along tile direction for tessellation at specified point.
     *
     * @param point point on the ellipsoid (Cartesian coordinates)
     * @param gp    point on the ellipsoid (geodetic coordinates)
     * @return normalized along tile direction
     */
    @Override
    native public Vector3D alongTileDirection(Vector3D point, GeodeticPoint gp);
}