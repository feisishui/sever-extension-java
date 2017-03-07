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

import com.esri.arcgis.carto.IMapLayerInfo;
import com.esri.arcgis.server.json.JSONObject;
import com.esri.serverextension.core.server.ServerObjectExtensionContext;
import com.esri.serverextension.core.util.ArcObjectsInteropException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Service
public class LayerResource {

    @RequestMapping("/layers/{layerId}")
    public JSONObject getLayerResource(@PathVariable("layerId") int layerId, ServerObjectExtensionContext serverContext) {
        IMapLayerInfo layerInfo = MapServerUtilities.getPointFeatureLayerByID(layerId, serverContext);
        JSONObject layerObject = new JSONObject();
        try {
            layerObject.put("name", layerInfo.getName());
            layerObject.put("id", layerInfo.getID());
            layerObject.put("description", layerInfo.getDescription());
        } catch (IOException ex) {
            throw new ArcObjectsInteropException(
                    String.format("Failed to get details for layer: %1$d", layerId));
        }
        return layerObject;
    }
}
