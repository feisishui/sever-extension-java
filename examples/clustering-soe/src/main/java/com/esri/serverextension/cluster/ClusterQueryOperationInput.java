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

import com.esri.serverextension.core.rest.api.Extent;
import com.esri.serverextension.core.rest.api.QueryMapServiceLayerOperationInput;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClusterQueryOperationInput extends QueryMapServiceLayerOperationInput {

    private static final long serialVersionUID = 1L;

    private Extent bbox;
    private Double mapUnitsPerPixel;
    private Integer clusterDistanceInPixels;
    private String clusterField;

    public ClusterQueryOperationInput() {
    }

    public Extent getBbox() {
        return bbox;
    }

    public void setBbox(Extent bbox) {
        this.bbox = bbox;
    }

    public Double getMapUnitsPerPixel() {
        return mapUnitsPerPixel;
    }

    public void setMapUnitsPerPixel(Double mapUnitsPerPixel) {
        this.mapUnitsPerPixel = mapUnitsPerPixel;
    }

    public Integer getClusterDistanceInPixels() {
        return clusterDistanceInPixels;
    }

    public void setClusterDistanceInPixels(Integer clusterDistanceInPixels) {
        this.clusterDistanceInPixels = clusterDistanceInPixels;
    }

    public String getClusterField() {
        return clusterField;
    }

    public void setClusterField(String clusterField) {
        this.clusterField = clusterField;
    }
}
