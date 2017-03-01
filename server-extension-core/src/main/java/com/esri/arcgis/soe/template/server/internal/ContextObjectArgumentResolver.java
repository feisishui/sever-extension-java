/*
 * Copyright (c) 2017 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.​
 */

package com.esri.arcgis.soe.template.server.internal;

import com.esri.arcgis.server.IServerObject;
import com.esri.arcgis.soe.template.security.SecurityContext;
import com.esri.arcgis.soe.template.server.RestDelegate;
import com.esri.arcgis.soe.template.server.RestRequest;
import com.esri.arcgis.soe.template.server.ServerObjectExtensionContext;
import com.esri.arcgis.system.IServerEnvironment;
import org.springframework.core.MethodParameter;

import java.security.Principal;
import java.util.Map;

public final class ContextObjectArgumentResolver implements ArgumentResolver {

    public ContextObjectArgumentResolver() {

    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  RestRequest request, RestDelegate handler) throws Exception {
        Class<?> type = parameter.getParameterType();
        if (RestRequest.class.isAssignableFrom(type)) {
            return request;
        } else if (RestDelegate.class.isAssignableFrom(type)) {
            return handler;
        } else if (ServerObjectExtensionContext.class.isAssignableFrom(type)) {
            return request.getServerContext();
        } else if (SecurityContext.class.isAssignableFrom(type)) {
            return request.getSecurityContext();
        } else if (Principal.class.isAssignableFrom(type)) {
            return request.getSecurityContext().getUserPrincipal();
        } else if (IServerObject.class.isAssignableFrom(type)) {
            return request.getServerContext().getServerObject();
        } else if (IServerEnvironment.class.isAssignableFrom(type)) {
            return request.getServerContext().getServerEnvironment();
        } else if (Map.class.isAssignableFrom(type)) {
            return request.getAttributes();
        }
        return null;
    }

    @Override
    public boolean resolves(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        return RestRequest.class.isAssignableFrom(type)
                || RestDelegate.class.isAssignableFrom(type)
                || ServerObjectExtensionContext.class.isAssignableFrom(type)
                || SecurityContext.class.isAssignableFrom(type)
                || Principal.class.isAssignableFrom(type)
                || IServerObject.class.isAssignableFrom(type)
                || IServerEnvironment.class.isAssignableFrom(type)
                || Map.class.isAssignableFrom(type);

    }
}
