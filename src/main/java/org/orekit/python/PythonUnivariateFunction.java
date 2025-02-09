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

// this file was created by SSC and is largely a derived work from the
// original file UnivariateFunction.java

package org.orekit.python;

/** import org.hipparchus.analysis.UnivariateFunction; **/


public class PythonUnivariateFunction implements org.hipparchus.analysis.UnivariateFunction {

	/** Part of JCC Python interface to object */
	protected long pythonObject;
	public void pythonExtension(long pythonObject) {
		this.pythonObject = pythonObject;
	}
	public long pythonExtension() {
		return this.pythonObject;
	}
	public void finalize() throws Throwable { pythonDecRef(); }
	public native void pythonDecRef();

	/** {@inheritDoc} */
	@Override
	public native double value(double x);

}

