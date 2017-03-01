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

import com.esri.arcgis.server.*;
import com.esri.serverextension.core.util.ArcObjectsInteropException;
import com.esri.arcgis.system.*;

import java.io.IOException;
import java.util.Map;

import static com.esri.serverextension.core.util.ArcObjectsUtilities.toMap;

public class ServerObjectExtensionContext {

    private final IServerObjectHelper serverObjectHelper;

    ServerObjectExtensionContext(IServerObjectHelper serverObjectHelper) {
        if (serverObjectHelper == null) {
            throw new NullPointerException(
                    "Argument 'serverObjectHelper' must not be null.");
        }
        this.serverObjectHelper = serverObjectHelper;
    }

    public static final ServerObjectExtensionContext create(
            IServerObjectHelper serverObjectHelper) {
        return new ServerObjectExtensionContext(serverObjectHelper);
    }

    public IServerObjectHelper getServerObjectHelper() {
        return serverObjectHelper;
    }

    public IServerObject getServerObject() {
        try {
            return serverObjectHelper.getServerObject();
        } catch (IOException ex) {
            throw new ArcObjectsInteropException(
                    "Failed to get server object.", ex);
        }
    }

    public Map<String, Object> getServerObjectProperties() {
        IPropertySet serverPropertySet;
        try {
            IServerObjectHelper2 helper = new IServerObjectHelper2Proxy(serverObjectHelper);
            serverPropertySet = helper.getServerObjectProperty();
        } catch (IOException ex) {
            throw new ArcObjectsInteropException(
                    "Failed to read server properties.", ex);
        }
        return toMap(serverPropertySet);
    }

    public IServerEnvironment getServerEnvironment() {
        try {
            EnvironmentManager envMgr = new EnvironmentManager();
            UID envUID = new UID();
            envUID.setValue("{32d4c328-e473-4615-922c-63c108f55e60}");
            IServerEnvironment serverEnvironment = new IServerEnvironment2Proxy(
                    envMgr.getEnvironment(envUID));
            Cleaner.release(envMgr);
            return serverEnvironment;
        } catch (Exception e) {
            throw new ServerObjectExtensionException(
                    "Exception when instantiating server IServerEnvironment", e);
        }
    }

    public Map<String, Object> getServerProperties() {
        IPropertySet serverPropertySet;
        try {
            IServerEnvironment serverEnvironment = getServerEnvironment();
            serverPropertySet = serverEnvironment.getProperties();
        } catch (IOException ex) {
            throw new ArcObjectsInteropException(
                    "Failed to read server properties.", ex);
        }
        return toMap(serverPropertySet);
    }

    public IServerObjectExtension getServerObjectExtension(String name) {
        IServerObject serverObject;
        try {
            serverObject = serverObjectHelper.getServerObject();
            IServerObjectExtensionManager extensionManager = new IServerObjectExtensionManagerProxy(
                    serverObject);
            IServerObjectExtension serverObjectExtension = extensionManager
                    .findExtensionByTypeName(name);
            if (serverObjectExtension != null) {
                return serverObjectExtension;
            }
        } catch (IOException ex) {
            throw new ArcObjectsInteropException(ex);
        }
        return null;
    }
}
