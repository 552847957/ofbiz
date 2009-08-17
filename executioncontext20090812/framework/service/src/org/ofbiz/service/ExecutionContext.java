/*
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
 */
package org.ofbiz.service;

import java.util.Map;

import org.ofbiz.security.SecurityConfigurationException;
import org.ofbiz.service.LocalDispatcher;

/**
 * ExecutionContext Interface. This interface extends the ExecutionContext
 * interface defined in the <code>security</code> component.
 */
public interface ExecutionContext extends org.ofbiz.security.ExecutionContext {

	/** Returns the current <code>LocalDispatcher</code> instance.
	 * 
	 * @return The current <code>LocalDispatcher</code> instance
	 */
	public LocalDispatcher getDispatcher();

	/** Initializes this ExecutionContext with artifacts found in
	 * <code>params</code>.
	 * 
	 * @param params
	 * @throws SecurityConfigurationException 
	 */
	public void initializeContext(Map<String, ? extends Object> params);

	/** Sets the current <code>LocalDispatcher</code> instance.
	 * 
	 * @param dispatcher The new <code>LocalDispatcher</code> instance
	 */
	public void setDispatcher(LocalDispatcher dispatcher);
}
