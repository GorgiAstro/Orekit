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

package org.orekit.data;

import org.orekit.data.DataLoader;
import org.orekit.data.DataProvider;

import java.util.regex.Pattern;

public class PythonDataProvider implements DataProvider {

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


    /** Feed a data file loader by browsing the data collection.
     * <p>
     * The method crawls all files referenced in the instance (for example
     * all files in a directories tree) and for each file supported by the
     * file loader it asks the file loader to load it.
     * </p>
     * <p>
     * If the method completes without exception, then the data loader
     * is considered to have been fed successfully and the top level
     * {@link DataProvidersManager data providers manager} will return
     * immediately without attempting to use the next configured providers.
     * </p>
     * <p>
     * If the method completes abruptly with an exception, then the top level
     * {@link DataProvidersManager data providers manager} will try to use
     * the next configured providers, in case another one can feed the
     * {@link DataLoader data loader}.
     * </p>
     *
     * <p> The default implementation will be removed in 11.0. It calls {@link
     * #feed(Pattern, DataLoader)}.
     *
     * @param supported pattern for file names supported by the visitor
     * @param visitor data file visitor to use
     * @param manager with the filters to apply to the resources.
     * @return true if some data has been loaded
     */
    @Override
    public native boolean feed(Pattern supported, DataLoader visitor, DataProvidersManager manager);
}
