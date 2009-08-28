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
package org.ofbiz.context;

import java.security.AccessControlException;
import java.security.Permission;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.entity.AccessController;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.ExecutionContext;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ModelService;

public class AccessControllerImpl<E> implements AccessController<E> {

    public static final String module = AccessControllerImpl.class.getName();

    protected final ExecutionContext executionContext;
    protected final OFBizPermission permission;
    protected final PathNode node;
    // Temporary - will be removed later
    protected boolean verbose = false;
    protected boolean disabled = false;

    protected AccessControllerImpl(ExecutionContext executionContext, PathNode node) {
        this.executionContext = executionContext;
        this.node = node;
        this.permission = new OFBizPermission(executionContext.getUserLogin().getString("userLoginId"));
        this.verbose = "true".equals(UtilProperties.getPropertyValue("api.properties", "authorizationManager.verbose"));
        this.disabled = "true".equals(UtilProperties.getPropertyValue("api.properties", "authorizationManager.disabled"));
    }

    public void checkPermission(Permission permission) throws AccessControlException {
        if (this.verbose) {
            Debug.logInfo("Checking permission: " + this.executionContext.getExecutionPath() + "[" + permission + "]", module);
        }
        this.permission.reset();
        this.node.getPermissions(this.executionContext.getExecutionPath(), this.permission);
        if (this.verbose) {
            Debug.logInfo("Found permission(s): " + this.executionContext.getUserLogin().getString("userLoginId") +
                    "@" + this.executionContext.getExecutionPath() + "[" + this.permission + "]", module);
        }
        if (this.disabled) {
            return;
        }
        if (this.permission.implies(permission) && this.hasServicePermission()) {
            return;
        }
        throw new AccessControlException(this.executionContext.getUserLogin().getString("userLoginId") +
                "@" + this.executionContext.getExecutionPath() + "[" + permission + "]");
    }

    public List<E> applyFilters(List<E> list) {
        if (this.permission.getFilterNames().size() > 0) {
            return new SecurityAwareList<E>(list, this.permission.getFilterNames(), this.executionContext);
        }
        return list;
    }

    public ListIterator<E> applyFilters(ListIterator<E> listIterator) {
        if (this.permission.getFilterNames().size() > 0) {
            return new SecurityAwareListIterator<E>(listIterator, this.permission.getFilterNames(), this.executionContext);
        }
        return listIterator;
    }

    public EntityListIterator applyFilters(EntityListIterator listIterator) {
        if (this.permission.getFilterNames().size() > 0) {
            // Commented out for now - causes problems with list pagination in UI
            //                return new SecurityAwareEli(listIterator, this.serviceNameList, this.executionContext);
        }
        return listIterator;
    }

    protected boolean hasServicePermission() {
        try {
            if (this.permission.getServiceNames().size() == 0) {
                return true;
            }
            LocalDispatcher dispatcher = this.executionContext.getDispatcher();
            DispatchContext ctx = dispatcher.getDispatchContext();
            Map<String, ? extends Object> params = this.executionContext.getParameters();
            for (String serviceName : this.permission.getServiceNames()) {
                ModelService modelService = ctx.getModelService(serviceName);
                Map<String, Object> context = FastMap.newInstance();
                if (params != null) {
                    context.putAll(params);
                }
                if (!context.containsKey("userLogin")) {
                    context.put("userLogin", this.executionContext.getUserLogin());
                }
                if (!context.containsKey("locale")) {
                    context.put("locale", this.executionContext.getLocale());
                }
                if (!context.containsKey("timeZone")) {
                    context.put("timeZone", this.executionContext.getTimeZone());
                }
                context = modelService.makeValid(context, ModelService.IN_PARAM);
                Map<String, Object> result = dispatcher.runSync(serviceName, context);
                Boolean hasPermission = (Boolean) result.get("hasPermission");
                if (hasPermission != null && !hasPermission.booleanValue()) {
                    return false;
                }
            }
        } catch (Exception e) {
            Debug.logError(e, module);
        }
        return true;
    }
}
