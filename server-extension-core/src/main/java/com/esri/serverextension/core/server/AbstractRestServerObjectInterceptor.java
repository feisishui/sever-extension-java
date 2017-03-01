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

import com.esri.arcgis.interop.AutomationException;
import com.esri.serverextension.core.rest.json.JSONConverter;
import com.esri.arcgis.system.IRESTRequestHandler;
import com.esri.arcgis.system.IRequestHandler;
import com.esri.arcgis.system.IRequestHandler2;
import com.esri.arcgis.system.IWebRequestHandler;

import java.io.IOException;

@SuppressWarnings("serial")
public abstract class AbstractRestServerObjectInterceptor extends
        AbstractRestServerObjectExtension implements IRequestHandler,
        IRequestHandler2, IWebRequestHandler {

    public AbstractRestServerObjectInterceptor() {
        super();
    }

    @Override
    public String getSchema() throws IOException, AutomationException {
        if (!isReady()) {
            getLogger().debug(
                    "Cannot handle schema request because server "
                            + "object extension is not ready.");
            return JSONConverter
                    .toString(handleError(new ServerObjectExtensionException(
                            "Cannot handle schema request because server "
                                    + "object extension is not ready.")));
        }

        getLogger().debug("Handling request for REST schema.");

        try {
            IRESTRequestHandler restRequestHandler = findRestRequestHandlerDelegate();
            if (restRequestHandler != null) {
                return restRequestHandler.getSchema();
            }

            getLogger()
                    .warn("No REST request handler found to service getSchema request.");
            return JSONConverter.toString(handleError(500,
                    "No REST request handler found to service schema request.",
                    null));
        } catch (Exception ex) {
            getLogger().error("Failed to handle schema request.", ex);
            return JSONConverter.toString(handleError(ex));
        } catch (java.lang.Error error) {
            getLogger().error(
                    "A fatal error occurred when handling schema request.",
                    error);
            return JSONConverter
                    .toString(handleError(
                            500,
                            "A fatal error occurred when handling schema request.",
                            null));
        }
    }

    @Override
    public byte[] handleBinaryRequest(byte[] request) throws IOException,
            AutomationException {
        // TODO: Send appropriate error response for binary request
        getLogger().warn("Binary request are not allowed.");
        return handleError(new ServerObjectExtensionException(
                "Binary request are not allowed."));
    }

    @Override
    public byte[] handleBinaryRequest2(String capabilities, byte[] request)
            throws IOException, AutomationException {
        // TODO: Send appropriate error response for binary request
        getLogger().warn("Binary request are not allowed.");
        return handleError(new ServerObjectExtensionException(
                "Binary request are not allowed."));
    }

    @Override
    public String handleStringRequest(String capabilities, String request)
            throws IOException, AutomationException {
        // TODO: Send appropriate error response for SOAP request
        getLogger().warn("SOAP requests are not allowed.");
        return JSONConverter
                .toString(handleError(new ServerObjectExtensionException(
                        "SOAP requests are not allowed.")));
    }

    @Override
    public byte[] handleStringWebRequest(int httpMethod, String requestURL,
                                         String queryString, String capabilities, String requestData,
                                         String[] responseContentType, int[] respDataType)
            throws IOException, AutomationException {
        // TODO: Send appropriate error response for OGC request
        getLogger().warn("WMS and WFS requests are not allowed.");
        return handleError(new ServerObjectExtensionException(
                "WMS and WFS requests are not allowed."));
    }
}
