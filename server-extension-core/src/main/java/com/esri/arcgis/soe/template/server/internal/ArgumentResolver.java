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

import com.esri.arcgis.soe.template.server.RestDelegate;
import com.esri.arcgis.soe.template.server.RestRequest;
import org.springframework.core.MethodParameter;

public interface ArgumentResolver {

    public Object resolveArgument(MethodParameter parameter,
                                  RestRequest request, RestDelegate handler) throws Exception;

    public boolean resolves(MethodParameter parameter);
}
