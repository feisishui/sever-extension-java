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
import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.geometry.IPoint;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.Cleaner;
import com.esri.arcgis.server.json.JSONObject;
import com.esri.serverextension.core.geodatabase.GeodatabaseTemplate;
import com.esri.serverextension.core.rest.api.*;
import com.esri.serverextension.core.rest.api.Feature;
import com.esri.serverextension.core.rest.api.Field;
import com.esri.serverextension.core.rest.api.FieldType;
import com.esri.serverextension.core.server.ServerObjectExtensionContext;
import com.esri.serverextension.core.util.ArcObjectsInteropException;
import com.esri.serverextension.core.util.GenericEsriEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.BeanParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClusterLayerResource {

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

    @RequestMapping("/layers/{layerId}/query")
    public FeatureSet query(@PathVariable("layerId") int layerId, @BeanParam ClusterQueryOperationInput input, ServerObjectExtensionContext serverContext) {
        try {
            IFeatureClass featureClass = MapServerUtilities.getPointFeatureClassByLayerID(layerId, serverContext);
            IQueryFilter queryFilter = getQueryFilter(input, featureClass.getShapeFieldName());
            GeodatabaseTemplate geodatabaseTemplate = new GeodatabaseTemplate();
            ClusterAssembler clusterAssembler = new ClusterAssembler(
                    input.getMapUnitsPerPixel(),
                    input.getClusterDistanceInPixels(),
                    input.getBbox(),
                    input.getClusterField()
            );
            ClusterAssemblerCallbackHandler clusterAssemblerCallbackHandler = new ClusterAssemblerCallbackHandler(clusterAssembler);
            geodatabaseTemplate.query(featureClass, queryFilter, clusterAssemblerCallbackHandler);
            FeatureSet featureSet = new FeatureSet();
            featureSet.setDisplayFieldName(input.getClusterField());
            Field field = new Field(input.getClusterField(),
                    FieldType.esriFieldTypeDouble, input.getClusterField());
            List<Field> fields = new ArrayList<>();
            fields.add(field);
            featureSet.setFields(fields);
            featureSet.setSpatialReference(getOutSpatialReference(input, serverContext));
            featureSet.setGeometryType(GeometryType.esriGeometryPoint);
            clusterAssembler.buildClusters();
            List<Cluster> clusters = clusterAssembler.getClusters();
            if (!CollectionUtils.isEmpty(clusters)) {
                List<Feature> features = new ArrayList<>(clusterAssembler.getClusters().size());
                for (Cluster cluster : clusterAssembler.getClusters()) {
                    if (cluster.getValue() == 0.0d) {
                        continue;
                    }
                    Feature clusterFeature = new Feature();
                    ClusterPoint clusterPoint = cluster.getPoint();
                    IPoint point = new Point();
                    point.setX(clusterPoint.x);
                    point.setY(clusterPoint.y);
                    clusterFeature.setGeometry(point);
                    Map<String, Object> attributes = new LinkedHashMap<>();
                    attributes.put(input.getClusterField().intern(), cluster.getValue());
                    clusterFeature.setAttributes(attributes);
                    features.add(clusterFeature);
                }
                featureSet.setFeatures(features);
            } else {
                List<Feature> features = new ArrayList<>(1);
                Feature clusterFeature = new Feature();
                Map<String, Object> attributes = new LinkedHashMap<>();
                attributes.put(input.getClusterField().intern(), clusterAssemblerCallbackHandler.getFeatureCount());
                clusterFeature.setAttributes(attributes);
                features.add(clusterFeature);
                featureSet.setFeatures(features);
            }
            return featureSet;
        } catch (IOException ex) {
            throw new ArcObjectsInteropException(
                    String.format("Failed to query cluster layer: %1$d", layerId));
        }
    }

    private IQueryFilter getQueryFilter(ClusterQueryOperationInput input, String shapeFieldName) {
        try {
            IQueryFilter2 queryFilter = null;
            if (input.getGeometry() != null) {
                SpatialFilter spatialFilter = new SpatialFilter();
                spatialFilter.setGeometryByRef(input.getGeometry());
                spatialFilter.setGeometryField(shapeFieldName);
                if (input.getSpatialRel() != null) {
                    spatialFilter.setSpatialRel(GenericEsriEnum.valueOf(esriSpatialRelEnum.class, input.getSpatialRel().name()));
                }
                if (StringUtils.isNotEmpty(input.getRelationParam())) {
                    spatialFilter.setSpatialRelDescription(input.getRelationParam());
                }
                if (input.getOutSR() != null) {
                    spatialFilter.setOutputSpatialReferenceByRef(shapeFieldName, input.getOutSR());
                }
                queryFilter = spatialFilter;
            } else {
                queryFilter = new QueryFilter();
            }
            if (StringUtils.isNotEmpty(input.getWhere())) {
                SQLCheck sqlCheck = new SQLCheck();
                sqlCheck.checkWhereClause(input.getWhere());
                Cleaner.release(sqlCheck);
                queryFilter.setWhereClause(input.getWhere());
            }
            if (StringUtils.isNotEmpty(input.getOrderByFields())) {
                ((IQueryFilterDefinition)queryFilter).setPostfixClause(String.format(
                 "ORDER BY %1$s", input.getOrderByFields()
                ));
            }
            queryFilter.setSpatialResolution(100000.0d);
            if (StringUtils.isNotEmpty(input.getClusterField())) {
                queryFilter.setSubFields(input.getClusterField());
                queryFilter.addField(shapeFieldName);
            }
            return queryFilter;
        } catch (IOException ex) {
            throw new ArcObjectsInteropException("Failed to create query filter.", ex);
        }
    }

    private ISpatialReference getOutSpatialReference(ClusterQueryOperationInput input, ServerObjectExtensionContext serverContext) {
        if (input.getOutSR() != null) {
            return input.getOutSR();
        }
        return MapServerUtilities.getMapSpatialReference(serverContext);
    }
}
