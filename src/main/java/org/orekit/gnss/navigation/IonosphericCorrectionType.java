/* Copyright 2023 Luc Maisonobe
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
package org.orekit.gnss.navigation;

/**
 * Ionospheric correction type.
 * @author Luc Maisonobe
 * @since 12.0
 */
public enum IonosphericCorrectionType {

    /** Galileo. */
    GAL,

    /** GPS α/β. */
    GPS,

    /** QZSSS α/β. */
    QZS,

    /** Beidou α/β. */
    BDS,

    /** IRNSS α/β. */
    IRN;

}
