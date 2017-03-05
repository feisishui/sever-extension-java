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

package com.esri.serverextension.core.server;

import com.esri.arcgis.addinframework.TypeChecker;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.interop.extn.ServerObjectExtProperties;
import com.esri.arcgis.server.IServerObject;
import com.esri.arcgis.server.IServerObjectExtension;
import com.esri.arcgis.server.IServerObjectHelper;
import com.esri.serverextension.core.rest.api.Error;
import com.esri.serverextension.core.rest.api.ErrorObject;
import com.esri.serverextension.core.server.internal.UriPath;
import com.esri.serverextension.core.util.ArcObjectsInteropException;
import com.esri.serverextension.core.rest.api.ArcGISServiceException;
import com.esri.serverextension.core.rest.json.JSONConverter;
import com.esri.serverextension.core.security.SecurityContext;
import com.esri.serverextension.core.util.ArcObjectsUtilities;
import com.esri.serverextension.core.util.StopWatch;
import com.esri.arcgis.system.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.Map.Entry;
import java.util.Set;

@SuppressWarnings("serial")
public abstract class AbstractRestServerObjectExtension implements
        IServerObjectExtension, IObjectConstruct, IRESTRequestHandler {

    private Logger logger = null;
    private ServerObjectExtensionContext serverContext;
    private String displayName;
    private ApplicationContext applicationContext;
    private RestDelegateMappings delegateMappings;
    private ObjectMapper objectMapper;

    private boolean isInitialized;
    private boolean isConstructed;
    private boolean isInterceptor;

    public AbstractRestServerObjectExtension() {
        super();
    }

    protected Logger getLogger() {
        return logger;
    }

    protected ServerObjectExtensionContext getServerContext() {
        return serverContext;
    }

    protected String getDisplayName() {
        return displayName;
    }

    @Override
    public final void init(IServerObjectHelper serverObjectHelper)
            throws IOException, AutomationException {
        try {
            // prevents server extension from failing to shut down
            Cleaner.trackObjectsInCurrentThread();

            ServerObjectExtProperties annotation = this.getClass()
                    .getAnnotation(ServerObjectExtProperties.class);
            isInterceptor = annotation.interceptor();
            displayName = annotation.displayName();
            logger = LoggerFactory.getLogger(getDisplayName());
            logger.info("Initializing ...");
            serverContext = ServerObjectExtensionContext
                    .create(serverObjectHelper);

            logger.info("Initialization completed.");
            isInitialized = true;
        } catch (Exception ex) {
            logger.error("Initialization failed.", ex);
        } catch (java.lang.Error error) {
            logger.error("A fatal error occurred during initialization.", error);
        }
    }

    @Override
    public final void shutdown() throws IOException, AutomationException {
        logger.info("Shutting down ...");
        doShutdown();
        ((ConfigurableApplicationContext) applicationContext).close();

        // prevents server extension from failing to shut down
        Cleaner.releaseAllInCurrentThread();

        logger.info("Shut down completed.");
        delegateMappings = null;
        serverContext = null;
        logger = null;
        displayName = null;
    }

    @Override
    public final void construct(IPropertySet propertySet) throws IOException {
        if (!isInitialized) {
            logger.info("Cannot construct server object extension because "
                    + "it is not initialized.");
            return;
        }

        try {
            logger.info("Constructing ...");

            Map<String, Object> configProperties = ArcObjectsUtilities.toMap(propertySet);
            Map<String, Object> serverProperties = serverContext
                    .getServerProperties();
            configProperties.putAll(serverProperties);

            if (logger.isDebugEnabled()) {
                StringBuilder configData = new StringBuilder(
                        "Configuration and environment properties: [");
                Iterator<Entry<String, Object>> entryIterator = configProperties
                        .entrySet().iterator();
                while (entryIterator.hasNext()) {
                    Entry<String, Object> entry = entryIterator.next();
                    configData.append(entry.getKey());
                    configData.append("=");
                    configData.append(entry.getValue());
                    if (entryIterator.hasNext()) {
                        configData.append(", ");
                    }
                }
                configData.append("]");
                logger.debug(configData.toString());
            }

            AnnotationConfigApplicationContext newContext = new AnnotationConfigApplicationContext();
            ConfigurableEnvironment environment = newContext.getEnvironment();
            MutablePropertySources propertySources = environment
                    .getPropertySources();
            propertySources.addFirst(new MapPropertySource("server",
                    serverProperties));
            propertySources.addAfter("server", new MapPropertySource("config",
                    configProperties));

            newContext.register(DefaultConfig.class);
            doConfigure(newContext);
            if (!newContext.isActive()) {
                newContext.refresh();
            }

            applicationContext = newContext;
            delegateMappings = applicationContext.getBean(
                    "delegateMappings", RestDelegateMappings.class);
            objectMapper = applicationContext.getBean("objectMapper",
                    ObjectMapper.class);

            logger.info("Construction completed.");

            isConstructed = true;
        } catch (Exception ex) {
            logger.error("Construction failed.", ex);
            throw new ServerObjectExtensionException("Construction failed.", ex);
        } catch (java.lang.Error error) {
            logger.error("A fatal error occured during construction.", error);
            throw new ServerObjectExtensionError(
                    "A fatal error occured during construction.", error);
        }
    }

    protected void doConfigure(
            AnnotationConfigApplicationContext applicationContext) {
    }

    protected void doShutdown() {
    }

    protected boolean isReady() {
        return isConstructed;
    }

    @Override
    public byte[] handleRESTRequest(String capabilities, String resourceName,
                                    String operationName, String operationInput, String outputFormat,
                                    String requestProperties, String[] responseProperties)
            throws IOException, AutomationException {

        if (!isReady()) {
            logger.debug("Cannot handle REST request because "
                    + "server object extension is not initialized.");
            return handleError(500, "Cannot handle REST request because "
                    + "server object extension is not initialized.", null);
        }

        logger.debug("Handling REST request ... ");

        try {
            Cleaner.trackObjectsInCurrentThread();
            StopWatch timer = StopWatch.createAndStart();

            RestRequest request = new RestRequest(capabilities, resourceName,
                    operationName, operationInput, outputFormat,
                    requestProperties, serverContext,
                    createSecurityContext(serverContext));
            Map<String, Object> serverProperties = serverContext
                    .getServerProperties();
            for (Entry<String, Object> entry : serverProperties.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }

            logger.debug("Request: {}", request);

            String path = new UriPath(resourceName, operationName).getPath();

            RestResponse response = null;
            RestDelegate delegate = delegateMappings.getMatchingDelegate(path);
            if (delegate != null) {
                logger.debug("Found delegate {} for path {}.", delegate, path);
                response = delegate.process(request, new RestDelegate() {
                    @Override
                    public RestResponse process(RestRequest request, RestDelegate handler) {
                        try {
                            return delegateRestRequestToHandler(request);
                        } catch (IOException ex) {
                            logger.debug(
                                    "Failed to delegate REST request to "
                                            + "server object: {}",
                                    request, ex);
                            throw new ServerObjectExtensionException(
                                    "Failed to delegate REST request to "
                                            + "server object.", ex);
                        }
                    }
                });
            } else {
                logger.debug("No delegate configured for '{}'. ", path);
                if (!isInterceptor) {
                    logger.warn("Cannot find delegate for path '{}'.", path);
                    throw new ServerObjectExtensionException(String.format("The resource/operation %1$s does not exist " +
                            "or access is forbidden.", path));
                } else {
                    logger.debug("Forwarding request to server object.");
                    response = delegateRestRequestToHandler(request);
                }
            }
            timer.stop();
            logger.debug("Finished handling REST request in {} second(s).",
                    timer.elapsedTimeSeconds());
            return response.getResponseBody();
        } catch (Exception ex) {
            logger.error("Failed to handle REST request.", ex);
            return handleError(ex);
        } catch (java.lang.Error error) {
            logger.error("A fatal error occurred when handling REST request.",
                    error);
            return handleError(500,
                    "A fatal error occurred when handling REST request.", null);
        } finally {
            Cleaner.releaseAllInCurrentThread();
        }
    }

    protected RestResponse delegateRestRequestToHandler(RestRequest request)
            throws IOException, AutomationException {
        IRESTRequestHandler restRequestHandler = findRestRequestHandlerDelegate();
        if (restRequestHandler != null) {
            logger.debug("Forwarding request to handler: {}", request);
            String[] responseProperties = new String[1];
            byte[] responseBody = restRequestHandler.handleRESTRequest(
                    request.getCapabilities(), request.getResourceName(),
                    request.getOperationName(), request.getOperationInput(),
                    request.getOutputFormat(), request.getRequestProperties(),
                    responseProperties);
            return new RestResponse(responseProperties[0], responseBody);
        } else {
            throw new ServerObjectExtensionException(
                    "Cannot find the correct REST request handler delegate.");
        }
    }

    protected IRESTRequestHandler findRestRequestHandlerDelegate() {
        try {
            IServerObject serverObject = serverContext.getServerObject();
            Map<String, Object> serverProperties = serverContext
                    .getServerProperties();
            // Check if there is an extension name set
            String extensionName = (String) serverProperties
                    .get("ExtensionName");
            if (extensionName == null || extensionName.isEmpty()) {
                // No extension has been set - return reference to parent parent
                // server object
                if (TypeChecker.instanceOf(serverObject,
                        IRESTRequestHandler.class)) {
                    return new IRESTRequestHandlerProxy(serverObject);
                } else {
                    throw new ServerObjectExtensionException(
                            "Server object does not implement IRESTRequestHandler.");
                }
            } else {
                IServerObjectExtension extension = serverContext
                        .getServerObjectExtension(extensionName);
                if (TypeChecker
                        .instanceOf(extension, IRESTRequestHandler.class)) {
                    return new IRESTRequestHandlerProxy(extension);
                } else {
                    throw new ServerObjectExtensionException(
                            "This server object extension does not "
                                    + "implement IRESTRequestHandler.");
                }
            }
        } catch (IOException ex) {
            throw new ServerObjectExtensionException(
                    "Failed to find REST request handler.", ex);
        }
    }

    protected byte[] handleError(Throwable t) throws IOException {
        Integer code = null;
        String message = null;
        String[] details = null;

        Throwable cause = t;
        Throwable root = null;
        while (cause != null) {
            root = cause;
            cause = cause.getCause();
        }

        if (root instanceof ArcGISServiceException) {
            ArcGISServiceException e = (ArcGISServiceException) root;
            code = e.getCode();
            message = e.getCauseMessage();
            details = e.getDetails();
        } else {
            message = root.getMessage();
        }

        if (code == null) {
            code = 500;
        }
        if (StringUtils.isEmpty(message)) {
            message = "Internal Server Error";
        }
        if (details == null) {
            details = new String[0];
        }

        return handleError(code, message, details);
    }

    protected byte[] handleError(int code, String message, String[] details)
            throws IOException {
        String errorJSON = objectMapper.writeValueAsString(new Error(
                new ErrorObject(code, message, details)));
        return JSONConverter.toByteArray(errorJSON);
    }

    private static final SecurityContext createSecurityContext(
            ServerObjectExtensionContext serverContext) {
        final String userName;
        final Set<String> userRoles;
        try {
            IServerEnvironment2 serverEnvironment = (IServerEnvironment2) serverContext
                    .getServerEnvironment();
            IServerUserInfo userInfo = serverEnvironment.getUserInfo();
            userName = "".equals(userInfo.getName()) ? "cpiepel" : userInfo.getName();

            Set<String> roleSet = new HashSet<String>();
            IEnumBSTR roles = userInfo.getRoles();
            if (roles != null) {
                roles.reset();
                String role = roles.next();
                while (!("".equals(role))) {
                    roleSet.add(role);
                    role = roles.next();
                }
            }
            userRoles = Collections.unmodifiableSet(roleSet);
        } catch (IOException ex) {
            throw new ArcObjectsInteropException(
                    "Failed to read server user info.");
        }

        final Principal principal = new Principal() {

            @Override
            public String getName() {
                return userName;
            }

            @Override
            public String toString() {
                return "Principal [name=" + userName + "]";
            }

        };

        return new SecurityContext() {

            @Override
            public Set<String> getUserRoles() {
                return userRoles;
            }

            @Override
            public Principal getUserPrincipal() {
                return principal;
            }

            @Override
            public String toString() {
                return "Principal [principal=" + principal + ", userRoles="
                        + userRoles + "]";

            }

        };
    }
}