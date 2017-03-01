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

package com.esri.arcgis.soe.template.rest.api;

import org.apache.commons.lang3.StringUtils;

public class ArcGISServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String causeMessage;
    private final Integer code;
    private final String[] details;

    public ArcGISServiceException(Integer code, String causeMessage,
                                  String[] details) {
        super(String.format("%1$d: %2$s [%3$s]", code, causeMessage,
                details != null ? StringUtils.join(details, ",") : ""));
        if (code == null) {
            throw new NullPointerException("Argument 'code' is required.");
        }
        if (causeMessage == null) {
            throw new NullPointerException(
                    "Argument 'causeMessage' is required.");
        }
        this.causeMessage = causeMessage;
        this.code = code;
        this.details = details;
    }

    public Integer getCode() {
        return code;
    }

    public String getCauseMessage() {
        return causeMessage;
    }

    public String[] getDetails() {
        return details;
    }
}
