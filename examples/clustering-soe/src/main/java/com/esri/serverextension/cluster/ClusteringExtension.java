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

package com.esri.serverextension.cluster;

import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.interop.extn.ArcGISExtension;
import com.esri.arcgis.interop.extn.ServerObjectExtProperties;
import com.esri.arcgis.server.json.JSONArray;
import com.esri.arcgis.server.json.JSONObject;
import com.esri.arcgis.system.ServerUtilities;
import com.esri.serverextension.core.server.AbstractRestServerObjectExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

@ArcGISExtension
@ServerObjectExtProperties(displayName = "Clustering",
        description = "Provides server-side clustering of point features.",
        interceptor = true,
        servicetype = "MapService")
public class ClusteringExtension extends AbstractRestServerObjectExtension {

    public static final String QUERY_OPERATION_PARAMETER_NAMES = "bbox, " +
            "mapUnitsPerPixel, clusterDistanceInPixels, clusterField, " +
            "geometry, geometryType, inSR, spatialRel, relationParam, where, " +
            "outField, outSR, orderByFields";

    @Override
    protected void doConfigure(
            AnnotationConfigApplicationContext applicationContext) {
        super.doConfigure(applicationContext);
        applicationContext.register(ClusteringConfig.class);
    }

    @Override
    protected void doShutdown() {
        super.doShutdown();
    }

    @Override
    public String getSchema() throws IOException, AutomationException {
        ServerObjectExtProperties annotation = this.getClass().getAnnotation(
                ServerObjectExtProperties.class);
        JSONObject rootResource = ServerUtilities.createResource(
                annotation.displayName(), annotation.description(), false,
                false);

        JSONArray resources = new JSONArray();
        rootResource.put("resources", resources);

        JSONArray layerResourceOperations = new JSONArray();

        JSONObject layerQueryOperation = ServerUtilities.createOperation(
                "query", QUERY_OPERATION_PARAMETER_NAMES, "json", false);
        layerResourceOperations.put(layerQueryOperation);

        JSONObject layersResource = ServerUtilities.createResource("layers",
                "Cluster layers", true, true);
        layersResource.put("operations", layerResourceOperations);
        resources.put(layersResource);

        JSONObject about = ServerUtilities.createResource("about",
                "About Clustering", false, false);
        resources.put(about);

        return rootResource.toString();
    }
}
