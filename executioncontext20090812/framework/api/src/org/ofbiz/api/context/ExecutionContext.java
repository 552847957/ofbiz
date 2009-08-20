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

/** ExecutionContext interface. */
public interface ExecutionContext {

    /** Returns an <code>AccessController</code> instance for this
     * user login and execution path combination.
     * 
     * @return An <code>AccessController</code> instance
     */
    public AccessController<?> getAccessController();

    /** Returns the currency unit of measure.
     * 
     * @return The ISO currency code
     */
    public String getCurrencyUom();

    /** Returns the current <code>ExecutionArtifact</code> (the one
     * at the top of the stack). Returns <code>null</code> if the
     * stack is empty.
     * 
     * @return The current <code>ExecutionArtifact</code>
     */
    public ExecutionArtifact getCurrentArtifact();

    /** Returns the current execution path. Artifacts in the path are separated
     * with a slash.
     * 
     * @return The current execution path
     */
    public String getExecutionPath();

    /** Returns the current <code>Locale</code>.
     * 
     * @return The current <code>Locale</code>
     */
    public Locale getLocale();

    /**
	 * Returns the parameters associated with this context.
	 * 
	 * @return The parameters associated with this context
	 */
	public Map<String, ? extends Object> getParameters();

    /** Returns the specified property.
     * 
     * @param key property whose associated value is to be returned
     * @return the specified property, or null if the property doesn't exist 
     */
    public Object getProperty(String key);

    /** Returns the current <code>TimeZone</code>.
     * 
     * @return The current <code>TimeZone</code>
     */
    public TimeZone getTimeZone();

    /** Pop an <code>ExecutionArtifact</code> off the stack. */
    public void popExecutionArtifact();

    /** Pops all <code>ExecutionArtifact</code>s off the stack
     * up to and including <code>artifact</code>. */
    public void popExecutionArtifacts(ExecutionArtifact artifact);

    /** Push an <code>ExecutionArtifact</code> on the stack.
     * 
     * @param artifact
     */
    public void pushExecutionArtifact(ExecutionArtifact artifact);

    /** Sets the currency unit of measure.
     * 
     * @param currencyUom The ISO currency code
     */
    public void setCurrencyUom(String currencyUom);

    /** Sets the current <code>Locale</code>.
     * 
     * @param locale The current <code>Locale</code>
     */
    public void setLocale(Locale locale);

    /** Associates the specified value with the specified key.
     * If the context contained a previous property for this key,
     * then the old value is replaced by the specified value.
     * 
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the previous value associated with specified key, or null  if there was no mapping for key
     */
    public Object setProperty(String key, Object value);

    /** Sets the current <code>TimeZone</code>.
     * 
     * @param timeZone The current <code>TimeZone</code>
     */
    public void setTimeZone(TimeZone timeZone);
}
