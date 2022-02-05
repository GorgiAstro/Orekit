/* Contributed in the public domain.
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

// this file was created by SCC 2020 and is largely a derived work from the
// original java class/interface


package org.orekit.files.general;

import org.orekit.data.DataSource;

import java.io.BufferedReader;
import java.io.IOException;

public class PythonAttitudeEphemerisFileParser implements AttitudeEphemerisFileParser {

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
     * Parse an attitude ephemeris file from a stream.
     *
     * @param reader   containing the ephemeris file.
     * @param fileName to use in error messages.
     * @return a parsed ephemeris file.
     * @throws IOException if {@code reader} throws one.
     */
    public native AttitudeEphemerisFile parse_BS(BufferedReader reader, String fileName) throws IOException;



    /**
     * Parse an attitude ephemeris file from a data source.
     *
     * @param source source providing the data to parse
     * @return a parsed attitude ephemeris file.
     */
    @Override
    public native AttitudeEphemerisFile<?, ?> parse(DataSource source);
}