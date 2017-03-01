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

import com.esri.serverextension.core.server.RestRequest;
import org.springframework.util.StringUtils;

public class UriPath {

    private final String resource;
    private final String operation;

    public UriPath(String resource, String operation) {
        this.resource = resource;
        this.operation = operation;
    }

    public UriPath(RestRequest request) {
        this(request.getResourceName(), request.getOperationName());
    }

    public String getPath() {
        // build the URI path to the requested resource and operation
        StringBuilder pathBuilder = new StringBuilder("/");
        if (!StringUtils.isEmpty(resource)) {
            pathBuilder.append(resource);
        }
        if (!StringUtils.isEmpty(operation)) {
            if (pathBuilder.length() > 0
                    && pathBuilder.charAt(pathBuilder.length() - 1) != '/') {
                pathBuilder.append('/');
            }
            pathBuilder.append(operation);
        }
        return pathBuilder.toString();
    }
}
