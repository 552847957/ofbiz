/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.ofbiz.api.context;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;

/** Implementation of the ExecutionContext interface. */
public abstract class ExecutionContextImpl implements ExecutionContext {

    public static final String module = ExecutionContextImpl.class.getName();

    protected final FastList<ExecutionArtifact> artifactStack = FastList.newInstance();
	protected String currencyUom = null;
	protected Locale locale = Locale.getDefault();
	protected TimeZone timeZone = TimeZone.getDefault();
	protected final Map<String, Object> properties;
	// Temporary - will be removed later
	protected boolean verbose = false;

	protected ExecutionContextImpl() {
	    this.properties = FastMap.newInstance();
	    this.verbose = "true".equals(UtilProperties.getPropertyValue("api.properties", "executionContext.verbose"));
	}
	
    protected ExecutionContextImpl(Map<String, Object> properties) {
        this.properties = properties;
        this.verbose = "true".equals(UtilProperties.getPropertyValue("api.properties", "executionContext.verbose"));
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.artifactStack.size() > 0) {
            // This check is temporary - it will be removed when implementation is complete
            Debug.logError(new Exception("finalize() called with a stack that is not empty"), module);
            return;
        }
        super.finalize();
    }

    public String getCurrencyUom() {
        return this.currencyUom;
    }

	public String getExecutionPath() {
		StringBuilder sb = new StringBuilder("ofbiz");
		for (ExecutionArtifact artifact : this.artifactStack) {
			sb.append("/");
			sb.append(artifact.getName() == null ? "null" : artifact.getName());
		}
		return sb.toString();
	}

	public Locale getLocale() {
        return this.locale;
    }

    public Map<String, ? extends Object> getParameters() {
    	for (int i = this.artifactStack.size() - 1; i >= 0; i--) {
    		try {
    			ParametersArtifact artifact = (ParametersArtifact) this.artifactStack.get(i);
    			return artifact.getParameters();
    		} catch (Exception e) {}
    	}
		return null;
	}

	public Object getProperty(String key) {
        return this.properties.get(key);
    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public void popExecutionArtifact() {
    	if (this.artifactStack.size() == 0) {
    		// This check is temporary - it will be removed when implementation is complete
    		Debug.logError(new Exception("Attempt to pop an empty stack"), module);
    		return;
    	}
	    ExecutionArtifact artifact = this.artifactStack.removeLast();
	    if (this.verbose) {
	    	Debug.logInfo("Popping artifact [" + artifact.getClass().getName() +
	    			"] location = " + artifact.getLocation() + 
	    			", name = " + artifact.getName(), module);
	    }
	}

    public void pushExecutionArtifact(ExecutionArtifact artifact) {
		this.artifactStack.addLast(artifact);
		if (this.verbose) {
			Debug.logInfo("Pushing artifact [" + artifact.getClass().getName() +
					"] location = " + artifact.getLocation() + 
					", name = " + artifact.getName(), module);
		}
	}

    public void setCurrencyUom(String currencyUom) {
        if (currencyUom != null) {
            this.currencyUom = currencyUom;
        }
    }

    public void setLocale(Locale locale) {
        if (locale != null) {
            this.locale = locale;
        }
    }

    public Object setProperty(String key, Object value) {
        return this.properties.put(key, value);
    }

    public void setTimeZone(TimeZone timeZone) {
        if (timeZone != null) {
            this.timeZone = timeZone;
        }
    }

    @Override
	public String toString() {
		return this.getExecutionPath();
	}
}
