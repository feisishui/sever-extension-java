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
import com.esri.serverextension.core.geodatabase.GeodatabaseObjectCallbackHandler;
import com.esri.serverextension.core.rest.api.Feature;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClusterAssemblerCallbackHandler implements GeodatabaseObjectCallbackHandler {

    private IField[] fields;
    private ClusterAssembler clusterAssembler;
    private int featureCount = 0;

    public ClusterAssemblerCallbackHandler(ClusterAssembler clusterAssembler) {
        this.clusterAssembler = clusterAssembler;
    }

    public int getFeatureCount() {
        return featureCount;
    }

    @Override
    public void setFields(IField[] fields) throws IOException {
        this.fields = fields;
    }

    @Override
    public void processRow(IRow row) throws IOException {
        throw new UnsupportedOperationException("This class only handles features.");
    }

    @Override
    public void processFeature(IFeature feature) throws IOException {
        featureCount++;
        Map<String, Object> attributes = new LinkedHashMap<>();
        for (int i = 0; i < fields.length; i++) {
            attributes.put(fields[i].getName(), feature.getValue(i));
        }
        Feature f = new Feature();
        f.setGeometry(feature.getShape());
        f.setAttributes(attributes);
        clusterAssembler.addFeature(f);
    }
}
