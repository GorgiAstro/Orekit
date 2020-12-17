/* Copyright 2002-2020 CS GROUP
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
package org.orekit.models.earth.troposphere;

/** Enumerate for Vienna tropospheric model 1 and 3.
 * This enumerate is used for the coefficients loader.
 * @see ViennaOneModel
 * @see ViennaThreeModel
 * @author Bryan Cazabonne
 */
public enum ViennaModelType {

    /** Vienna one tropospheric model. */
    VIENNA_ONE,

    /** Vienna three tropospheric model. */
    VIENNA_THREE;

}