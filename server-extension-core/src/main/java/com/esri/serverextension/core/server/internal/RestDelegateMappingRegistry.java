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
import com.esri.serverextension.core.server.RestDelegateMappings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriTemplate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RestDelegateMappingRegistry extends
        ApplicationObjectSupport implements RestDelegateMappings,
        InitializingBean {

    private final Logger logger = LoggerFactory
            .getLogger(RestDelegateMappingRegistry.class);

    private final List<RestDelegateMappingEntry> mappings;
    private final List<ArgumentResolver> argumentResolvers;
    private final List<ReturnValueHandler> returnValueHandlers;

    @Autowired(required = true)
    private ObjectMapper objectMapper;
    private boolean isSorted;

    public RestDelegateMappingRegistry(ObjectMapper objectMapper) {
        if (objectMapper == null) {
            throw new NullPointerException(
                    "Argument 'objectMapper' is required.");
        }
        this.objectMapper = objectMapper;
        mappings = new ArrayList<RestDelegateMappingEntry>();
        argumentResolvers = new ArrayList<ArgumentResolver>();
        returnValueHandlers = new ArrayList<ReturnValueHandler>();
    }

    public void addArgumentResolver(ArgumentResolver argumentResolver) {
        argumentResolvers.add(argumentResolver);
    }

    public boolean removeArgumentResolver(ArgumentResolver argumentResolver) {
        return argumentResolvers.remove(argumentResolver);
    }

    public void clearArgumentResolvers(ArgumentResolver argumentResolver) {
        argumentResolvers.clear();
    }

    protected List<ArgumentResolver> getArgumentResolvers() {
        return argumentResolvers;
    }

    public void configureDefaultArgumentResolvers() {
        addReturnValueHandler(new DefaultReturnValueHandler(objectMapper));
    }

    public void addReturnValueHandler(ReturnValueHandler returnValueHandler) {
        returnValueHandlers.add(returnValueHandler);
    }

    public boolean removeReturnValueHandler(
            ReturnValueHandler returnValueHandler) {
        return returnValueHandlers.remove(returnValueHandler);
    }

    public void clearReturnValueHandlers(ReturnValueHandler returnValueHandler) {
        returnValueHandlers.clear();
    }

    protected List<ReturnValueHandler> getReturnValueHandlers() {
        return returnValueHandlers;
    }

    public void configureDefaultReturnValueHandlers() {
        addArgumentResolver(new PathVariableArgumentResolver());
        addArgumentResolver(new RequestParamArgumentResolver(objectMapper));
        addArgumentResolver(new BeanParamArgumentResolver(objectMapper));
        addArgumentResolver(new ContextObjectArgumentResolver());
        addArgumentResolver(new RequestBodyArgumentResolver(objectMapper));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        configureDefaultArgumentResolvers();
        configureDefaultReturnValueHandlers();
        findAndRegisterAnnotatedDelegates(getApplicationContext());
    }

    private void findAndRegisterAnnotatedDelegates(
            ApplicationContext applicationContext) {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            if (bean != null) {
                Class<?> type = bean.getClass();
                Method[] methods = type.getMethods();
                for (Method method : methods) {
                    findAndRegisterAnnotatedDelegateMethods(bean, method);
                }
            }
        }
    }

    private void findAndRegisterAnnotatedDelegateMethods(Object bean,
                                                         Method method) {
        RequestMapping requestMapping = method
                .getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            String[] paths = requestMapping.value();
            for (String path : paths) {
                if (path != null && !path.isEmpty()) {
                    DelegateMethodInvoker delegate = new DelegateMethodInvoker(
                            bean, method,
                            findMatchingArgumentResolvers(method),
                            findMatchingReturnValueHandler(method));
                    Order order = method.getAnnotation(Order.class);
                    if (order == null) {
                        registerDelegate(path, delegate);
                    } else {
                        registerDelegate(path, delegate, order.value());
                    }
                }
            }
        }
    }

    private List<ArgumentResolver> findMatchingArgumentResolvers(Method method) {
        List<ArgumentResolver> matchingResolvers = new ArrayList<ArgumentResolver>();
        int numParameters = method.getParameterTypes().length;
        for (int i = 0; i < numParameters; i++) {
            ArgumentResolver matchingResolver = null;
            for (ArgumentResolver candidateResolver : getArgumentResolvers()) {
                MethodParameter methodParameter = new MethodParameter(method, i);
                if (candidateResolver.resolves(methodParameter)) {
                    matchingResolver = candidateResolver;
                    break;
                }
            }
            if (matchingResolver == null) {
                throw new IllegalStateException(String.format(
                        "Cannot find matching argument resolver for "
                                + "parameter %1$d of method %2$s in %3$s.", i,
                        method, method.getClass()));
            }
            matchingResolvers.add(matchingResolver);
        }
        return matchingResolvers;
    }

    private ReturnValueHandler findMatchingReturnValueHandler(Method method) {
        ReturnValueHandler matchingHandler = null;
        for (ReturnValueHandler candidateHandler : getReturnValueHandlers()) {
            MethodParameter methodParameter = new MethodParameter(method, -1);
            if (candidateHandler.handles(methodParameter)) {
                matchingHandler = candidateHandler;
                break;
            }
        }
        if (matchingHandler == null) {
            throw new IllegalStateException(
                    String.format(
                            "Cannot find matching return value handler for "
                                    + "method %1$s in %2$s.", method,
                            method.getClass()));
        }

        return matchingHandler;
    }

    @Override
    public void registerDelegate(String path, RestDelegate delegate) {
        registerDelegate(path, delegate, 1);
    }

    @Override
    public void registerDelegate(String path,
                                 RestDelegate delegate, int priority) {
        registerDelegate(new UriTemplate(path), delegate, priority);
    }

    @Override
    public void registerDelegate(UriTemplate path,
                                 RestDelegate restInterceptor) {
        registerDelegate(path, restInterceptor, 1);
    }

    @Override
    public void registerDelegate(UriTemplate path,
                                 RestDelegate delegate, int priority) {
        if (path == null) {
            throw new NullPointerException("Argument 'path' is required.");
        }
        if (delegate == null) {
            throw new NullPointerException(
                    "Argument 'delegate' is required.");
        }
        logger.debug(
                "Registering delegate '{}' for path {} with priority {}.",
                delegate, path, priority);
        synchronized (mappings) {
            mappings.add(new RestDelegateMappingEntry(path, delegate,
                    priority));
            isSorted = false;
        }
    }

    public static final class RestDelegateMappingEntry implements
            Comparable<RestDelegateMappingEntry> {

        private final UriTemplate path;
        private final RestDelegate delegate;
        private final int priority;

        public RestDelegateMappingEntry(UriTemplate path,
                                        RestDelegate delegate, int priority) {
            this.path = path;
            this.delegate = delegate;
            this.priority = priority;
        }

        public UriTemplate getPath() {
            return path;
        }

        public RestDelegate getDelegate() {
            return delegate;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public String toString() {
            return "Entry [path=" + path + ", delegate="
                    + delegate + ", priority=" + priority + "]";
        }

        @Override
        public int compareTo(RestDelegateMappingEntry o) {
            int c = Integer.compare(priority, o.priority);
            if (c == 0) {
                if (path == null && o.path == null) {
                    return 0;
                } else if (path == null) {
                    return -1;
                } else if (o.path == null) {
                    return 1;
                } else {
                    int x = path.toString().length();
                    int y = o.path.toString().length();
                    if (x == y) {
                        return path.toString().compareTo(o.path.toString());
                    } else {
                        return Integer.compare(x, y);
                    }
                }
            }
            return c;
        }
    }

    @Override
    public RestDelegate getMatchingDelegate(String path) {
        if (!isSorted) {
            synchronized (mappings) {
                Collections.sort(mappings);
                Collections.reverse(mappings);
                isSorted = true;
            }
        }
        for (RestDelegateMappingEntry entry : mappings) {
            if (entry.getPath().matches(path)) {
                return entry.getDelegate();
            }
        }
        return null;
    }
}
