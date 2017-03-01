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

import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.interop.extn.ArcGISExtension;
import com.esri.arcgis.interop.extn.ServerObjectExtProperties;
import com.esri.arcgis.server.json.JSONArray;
import com.esri.arcgis.server.json.JSONException;
import com.esri.arcgis.server.json.JSONObject;
import com.esri.serverextension.core.server.AbstractRestServerObjectExtension;
import com.esri.arcgis.system.ServerUtilities;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

@ArcGISExtension
@ServerObjectExtProperties(displayName = "My SOE",
        description = "A simple template server object extension",
        interceptor = false,
        servicetype = "MapService")
public class MyServerObjectExtension extends AbstractRestServerObjectExtension {

    @Override
    protected void doConfigure(
            AnnotationConfigApplicationContext applicationContext) {
        super.doConfigure(applicationContext);

        applicationContext.register(MyServerObjectExtensionConfig.class);
    }

    @Override
    protected void doShutdown() {
        super.doShutdown();
    }

    @Override
    public String getSchema() throws IOException, AutomationException {
        try {
            JSONObject mySOE = ServerUtilities.createResource(
                    "My SOE", "A simple template server object extension", false, false);
            JSONArray subResourcesArray = new JSONArray();
            subResourcesArray.put(ServerUtilities.createResource("hello",
                    "Hello resource", false, false));
            mySOE.put("resources", subResourcesArray);
            return mySOE.toString();
        } catch (JSONException e) {
            getLogger().debug(e.getMessage());
            return ServerUtilities.sendError(500,
                    "Exception occurred: " + e.getMessage(), null);
        }
    }
}
