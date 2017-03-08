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

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IField;
import com.esri.arcgis.geodatabase.IRow;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IPoint;
import com.esri.serverextension.core.geodatabase.GeodatabaseFieldMap;
import com.esri.serverextension.core.geodatabase.GeodatabaseObjectCallbackHandler;
import com.esri.serverextension.core.geodatabase.GeodatabaseObjectMapper;
import com.esri.serverextension.core.rest.api.Feature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClusterAssemblerCallbackHandler implements GeodatabaseObjectCallbackHandler {

    private GeodatabaseFieldMap fieldMap;
    private String clusterFieldName;
    private int clusterFieldIndex = -1;
    private int featureCount = 0;
    private ArrayList<ClusterFeature> clusterFeatures = new ArrayList<>();

    public ClusterAssemblerCallbackHandler(String clusterFieldName) {
        this.clusterFieldName = clusterFieldName;
    }

    public int getFeatureCount() {
        return featureCount;
    }

    public ArrayList<ClusterFeature> getClusterFeatures() {
        return clusterFeatures;
    }

    @Override
    public void setGeodatabaseFieldMap(GeodatabaseFieldMap fieldMap) throws IOException {
        this.fieldMap = fieldMap;
        clusterFieldIndex = fieldMap.get(clusterFieldName).getIndex();
    }

    @Override
    public void processRow(IRow row) throws IOException {
        throw new UnsupportedOperationException("This callback handler only supports features.");
    }

    @Override
    public void processFeature(IFeature feature) throws IOException {
        featureCount++;
        Map<String, Object> attributes = new LinkedHashMap<>();
        for (GeodatabaseFieldMap.FieldIndex fieldIndex : fieldMap.getFieldIndices()) {
            attributes.put(fieldIndex.getField().getName(), feature.getValue(fieldIndex.getIndex()));
        }
        IGeometry geometry = feature.getShape();
        if (geometry instanceof IPoint && !geometry.isEmpty()) {
            IPoint point = (IPoint)geometry;
            ClusterPoint clusterPoint = new ClusterPoint(point.getX(), point.getY());
            Object value = feature.getValue(clusterFieldIndex);
            if (value == null) {
                return;
            }
            if (value instanceof Number) {
                ClusterFeature clusterFeature = new ClusterFeature(clusterPoint,
                        ((Number) value).doubleValue());
                clusterFeatures.add(clusterFeature);
            }
        }
    }
}
