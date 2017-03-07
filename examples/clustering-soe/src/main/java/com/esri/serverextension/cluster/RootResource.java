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
import com.esri.arcgis.interop.extn.ServerObjectExtProperties;
import com.esri.arcgis.server.json.JSONArray;
import com.esri.arcgis.server.json.JSONObject;
import com.esri.serverextension.core.server.ServerObjectExtensionContext;
import com.esri.serverextension.core.util.ArcObjectsInteropException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Service
public class RootResource {

	@RequestMapping("/")
	public JSONObject getRootResource(ServerObjectExtensionContext serverContext) {
		ServerObjectExtProperties annotation = ClusteringExtension.class
				.getAnnotation(ServerObjectExtProperties.class);

		JSONObject rootResource = new JSONObject();
		rootResource.put("name", annotation.displayName());
		rootResource.put("description", annotation.description());

		JSONArray layersArray = new JSONArray();
		List<IMapLayerInfo> pointFeatureLayers = MapServerUtilities.getPointFeatureLayers(serverContext);
		for (IMapLayerInfo layerInfo : pointFeatureLayers) {
			try {
				JSONObject layer = new JSONObject();
				layer.put("name", layerInfo.getName());
				layer.put("id", layerInfo.getID());
				layer.put("description", layerInfo.getDescription());
				layersArray.put(layer);
			} catch (IOException ex) {
				throw new ArcObjectsInteropException(
						"Failed to get details from map layer info.");
			}
		}
		rootResource.put("layers", layersArray);
		return rootResource;
	}
}
