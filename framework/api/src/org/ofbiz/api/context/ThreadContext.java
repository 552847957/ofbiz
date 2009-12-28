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

import org.ofbiz.api.authorization.AccessController;
import org.ofbiz.api.authorization.AuthorizationManager;
import org.ofbiz.api.context.ExecutionArtifact;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;

/** A convenience class for accessing the current thread's <code>ExecutionContext</code>.
 * @see {@link org.ofbiz.service.ExecutionContext} 
 */
public class ThreadContext {

    protected static final String module = ThreadContext.class.getName();

    protected static final ThreadLocal<ExecutionContext> executionContext = new ThreadLocal<ExecutionContext>() {
        protected synchronized ExecutionContext initialValue() {
            ExecutionContext result = null;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            String className = UtilProperties.getPropertyValue("api.properties", "executionContext.class");
            try {
                result = (ExecutionContext) loader.loadClass(className).newInstance();
            } catch (Exception e) {
                Debug.logError(e, module);
            }
            return result;
        }
    };

    public static AccessController getAccessController() {
        return executionContext.get().getAccessController();
    }

    public static String getCurrencyUom() {
        return executionContext.get().getCurrencyUom();
    }

    public static String getExecutionPath() {
        return executionContext.get().getExecutionPath();
    }

    public static Locale getLocale() {
        return executionContext.get().getLocale();
    }

    public static Map<String, ? extends Object> getParameters() {
        return executionContext.get().getParameters();
    }

    public static Object getProperty(String key) {
        return executionContext.get().getProperty(key);
    }

    public static AuthorizationManager getSecurity() {
        return executionContext.get().getSecurity();
    }

    public static TimeZone getTimeZone() {
        return executionContext.get().getTimeZone();
    }

    public static void initializeContext(Map<String, ? extends Object> params) {
        executionContext.get().initializeContext(params);
    }

    public static void popExecutionArtifact() {
        executionContext.get().popExecutionArtifact();
    }

    public static void pushExecutionArtifact(ExecutionArtifact artifact) {
        executionContext.get().pushExecutionArtifact(artifact);
    }

    public static void reset() {
        executionContext.get().reset();
    }

    public static void setCurrencyUom(String currencyUom) {
        executionContext.get().setCurrencyUom(currencyUom);
    }

    public static void setLocale(Locale locale) {
        executionContext.get().setLocale(locale);
    }

    public static Object setProperty(String key, Object value) {
        return executionContext.get().setProperty(key, value);
    }

    public static void setSecurity(AuthorizationManager security) {
        executionContext.get().setSecurity(security);
    }

    public static void setTimeZone(TimeZone timeZone) {
        executionContext.get().setTimeZone(timeZone);
    }

}
