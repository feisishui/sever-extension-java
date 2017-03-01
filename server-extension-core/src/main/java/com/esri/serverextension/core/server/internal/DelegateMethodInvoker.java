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
import com.esri.serverextension.core.server.RestResponse;
import com.esri.serverextension.core.server.ServerObjectExtensionException;
import org.springframework.core.MethodParameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public final class DelegateMethodInvoker implements RestDelegate {

    private final Object bean;
    private final Method method;
    private final List<ArgumentResolver> argumentResolvers;
    private final ReturnValueHandler returnValueHandler;

    public DelegateMethodInvoker(Object bean, Method method,
                                 List<ArgumentResolver> argumentResolvers,
                                 ReturnValueHandler returnValueHandler) {
        this.bean = bean;
        this.method = method;
        this.argumentResolvers = argumentResolvers;
        this.returnValueHandler = returnValueHandler;
    }

    @Override
    public RestResponse process(RestRequest request, RestDelegate handler) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            MethodParameter parameter = new MethodParameter(method, i);
            ArgumentResolver resolver = argumentResolvers.get(i);
            try {
                arguments[i] = resolver.resolveArgument(parameter, request, handler);
            } catch (Exception ex) {
                String className = method.getDeclaringClass().toString();
                String methodName = method.toString();
                throw new ServerObjectExtensionException(
                        String.format(
                                "Exception when resolving delegate method "
                                        + "argument '%3$d' for method '%1$s' in '%2$s'.",
                                className, methodName, i), ex);
            }

        }
        MethodParameter returnParameter = new MethodParameter(method, -1);
        Object returnValue;
        try {
            returnValue = method.invoke(bean, arguments);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ex) {
            String className = method.getDeclaringClass().toString();
            String methodName = method.toString();
            String message = String.format(
                    "Exception when invoking delegate method "
                            + "'%1$s' in '%2$s' with the following "
                            + "arguments: %3$s", methodName, className,
                    Arrays.toString(arguments));
            throw new ServerObjectExtensionException(message, ex);
        }
        RestResponse response = null;
        try {
            response = returnValueHandler.handleReturnValue(returnValue,
                    returnParameter, request);
        } catch (Exception ex) {
            String className = method.getDeclaringClass().toString();
            String methodName = method.toString();
            String message = String
                    .format("Exception when handling return value '%1$s' from "
                                    + "delegate method '%2$s' in '%3$s' with the following "
                                    + "arguments: %4$s", returnValue, methodName,
                            className, Arrays.toString(arguments));
            throw new ServerObjectExtensionException(message, ex.getCause());
        }

        return response;
    }

    @Override
    public String toString() {
        String beanName = null;
        if (bean != null) {
            beanName = bean.getClass().getName();
        }
        String methodName = null;
        if (method != null) {
            methodName = method.toString();
        }
        return "DelegateMethodInvoker [bean=" + beanName + ", method="
                + methodName + "]";
    }

}
