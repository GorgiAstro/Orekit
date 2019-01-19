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

package org.orekit.python;

import org.hipparchus.ode.AbstractIntegrator;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.propagation.conversion.ODEIntegratorBuilder;

public class PythonODEIntegratorBuilder implements ODEIntegratorBuilder {
    /**
     * Build a first order integrator.
     *
     * @param orbit     reference orbit
     * @param orbitType orbit type to use
     * @return a first order integrator ready to use
     */
    @Override
    public native AbstractIntegrator buildIntegrator(Orbit orbit, OrbitType orbitType);
}
