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

package com.esri.serverextension.core.server.internal;

import com.esri.serverextension.core.server.RestDelegate;
import com.esri.serverextension.core.server.RestRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;

import javax.ws.rs.BeanParam;

public final class BeanParamArgumentResolver implements ArgumentResolver {

    private final ObjectMapper objectMapper;

    public BeanParamArgumentResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  RestRequest request, RestDelegate handler) throws Exception {
        Class<?> type = parameter.getParameterType();
        BeanParam annotation = parameter
                .getParameterAnnotation(BeanParam.class);
        if (annotation != null) {
            return objectMapper.readValue(request.getOperationInput(), type);
        }
        return null;
    }

    @Override
    public boolean resolves(MethodParameter parameter) {
        return parameter.getParameterAnnotation(BeanParam.class) != null;
    }
}
