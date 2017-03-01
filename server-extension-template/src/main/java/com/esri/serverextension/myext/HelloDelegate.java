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

package com.esri.serverextension.myext;

import com.esri.arcgis.server.json.JSONObject;
import com.esri.serverextension.core.server.RestRequest;
import com.esri.serverextension.core.server.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Component
public class HelloDelegate {

    protected final Logger logger = LoggerFactory.getLogger(HelloDelegate.class);

    @RequestMapping(value = {"/hello"})
    public RestResponse handleHelloResourceRequest(
            RestRequest request) throws IOException {
        logger.debug("Handling hello resource request ...");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Hello, World!");
        byte[] data = jsonObject.toString().getBytes("utf-8");

        return new RestResponse(null, data);
    }
}
