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

package com.esri.serverextension.core.server;

import com.esri.arcgis.carto.*;
import com.esri.arcgis.geodatabase.*;
import com.esri.serverextension.core.util.ArcObjectsInteropException;

import java.io.IOException;

public final class MapServerUtilities {

    private MapServerUtilities() {
    }

    public static final IWorkspace findFirstWorkspaceContainingDataset(
            ServerObjectExtensionContext serverContext, int esriDatasetType, String datasetName) {
        try {
            IMapServerDataAccess mapServerDataAccess = (IMapServerDataAccess) serverContext
                    .getServerObject();
            IMapServer3 mapServer = (IMapServer3) mapServerDataAccess;
            String mapName = mapServer.getDefaultMapName();
            IMapServerInfo4 mapServerInfo = (IMapServerInfo4) mapServer
                    .getServerInfo(mapName);
            IMapLayerInfos layerInfos = mapServerInfo.getMapLayerInfos();
            int layerCount = layerInfos.getCount();
            for (int i = 0; i < layerCount; i++) {
                IMapLayerInfo layerInfo = layerInfos.getElement(i);
                if (layerInfo.isComposite()) {
                    continue;
                }
                if (layerInfo.isFeatureLayer()) {
                    Object dataSource = mapServerDataAccess
                            .getDisplayDataSource(mapName, layerInfo.getID());
                    IFeatureClass featureClass = new FeatureClass(dataSource);
                    IDataset dataset = (IDataset) featureClass;
                    IWorkspace workspace = (IWorkspace) dataset.getWorkspace();
                    IWorkspace2Proxy workspace2 = new IWorkspace2Proxy(workspace);
                    if (workspace2.isNameExists(esriDatasetType, datasetName)) {
                        return workspace;
                    }
                }
            }

            IStandaloneTableInfos tableInfos = mapServerInfo
                    .getStandaloneTableInfos();
            if (tableInfos.getCount() > 0) {
                IStandaloneTableInfo tableInfo = tableInfos.getElement(0);
                Object dataSource = mapServerDataAccess.getDisplayDataSource(
                        mapName, tableInfo.getID());
                ITable table = new Table(dataSource);
                IDataset dataset = (IDataset) table;
                IWorkspace workspace = (IWorkspace) dataset.getWorkspace();
                IWorkspace2Proxy workspace2 = new IWorkspace2Proxy(workspace);
                if (workspace2.isNameExists(esriDatasetType, datasetName)) {
                    return workspace;
                }
            }
            return null;
        } catch (IOException ex) {
            throw new ArcObjectsInteropException(String.format(
                    "Failed to get first available workspace from "
                            + "map server object that contains datatset %1$s (%2$d).", datasetName, esriDatasetType));
        }
    }
}
