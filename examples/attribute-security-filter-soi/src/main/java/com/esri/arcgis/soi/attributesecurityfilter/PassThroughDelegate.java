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
package com.esri.arcgis.soi.attributesecurityfilter;

import com.esri.arcgis.soe.template.server.RestDelegate;
import com.esri.arcgis.soe.template.server.RestRequest;
import com.esri.arcgis.soe.template.server.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Component
public class PassThroughDelegate {

    protected final Logger logger = LoggerFactory.getLogger(PassThroughDelegate.class);

    @RequestMapping(value = {"/", "/layers/{id}"})
    public RestResponse query(
            RestRequest request,
            RestDelegate handler) throws IOException {
        logger.debug("Passing through request ...");

        return handler.process(request, null);
    }
}
