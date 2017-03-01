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

import com.esri.arcgis.soe.template.server.RestRequest;
import com.esri.arcgis.soe.template.server.RestResponse;
import org.springframework.core.MethodParameter;

public interface ReturnValueHandler {

    public RestResponse handleReturnValue(Object returnValue,
                                          MethodParameter parameter, RestRequest request) throws Exception;

    public boolean handles(MethodParameter parameter);
}