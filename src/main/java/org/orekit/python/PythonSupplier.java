/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// this file was created by Petrus Hyvönen, SSC, 2022

package org.orekit.python;
import java.util.function.Supplier;

/** A wrapper of the import java.util.function.Supplier Interface **/


public class PythonSupplier implements Supplier
		{

	static final long serialVersionUID = 1L;

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
			 * Gets a result.
			 *
			 * @return a result
			 */
			@Override
			public native Object get();
		}

