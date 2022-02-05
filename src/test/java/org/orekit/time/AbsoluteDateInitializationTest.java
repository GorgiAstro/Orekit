/* Copyright 2002-2022 CS GROUP
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
package org.orekit.time;

import org.junit.Assert;
import org.junit.Test;
import org.orekit.Utils;

/**
 * Only reliably tests initialization if only a single test method is run.
 *
 * @author Evan Ward
 */
public class AbsoluteDateInitializationTest {


    @Test
    public void testAbsoluteDateInitializationWithoutLeapSeconds() {
        // setup
        Utils.setDataRoot("no-data");

        // just some code that makes an assertion using AbsoluteDate,
        // the real code under test is AbsoluteDate initialization.
        AbsoluteDate date = AbsoluteDate.J2000_EPOCH;
        Assert.assertEquals(new AbsoluteDate(date, 10).durationFrom(date), 10.0, 0.0);
    }

}
