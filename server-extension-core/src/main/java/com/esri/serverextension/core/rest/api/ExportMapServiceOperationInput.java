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

package com.esri.serverextension.core.rest.api;

import com.esri.arcgis.geometry.ISpatialReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 * kyunam_kim@esri.com on 1/11/17.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExportMapServiceOperationInput extends OperationInput {

  private static final long serialVersionUID = 1L;

  private String bbox;
  private String size;
  private ISpatialReference imageSR;
  private ISpatialReference bboxSR;
  private String format;
  private String time;
  private Integer dpi;
  private Map layerDefs;
  private String layers;
  @JsonProperty("transparent")
  private boolean transparent;
  private Map layerTimeOptions;
  private Map dynamicLayers;
  private String gdbVersion;
  private Double mapScale;
  private Double rotation;
  private List<Map> datumTransformations;
  private List<Map> mapRangeValues;
  private Map layerRangeValues;
  private List<Map> layerParameterValues;

  public String getBbox() {
    return bbox;
  }

  public void setBbox(String bbox) {
    this.bbox = bbox;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public ISpatialReference getImageSR() {
    return imageSR;
  }

  public void setImageSR(ISpatialReference imageSR) {
    this.imageSR = imageSR;
  }

  public ISpatialReference getBboxSR() {
    return bboxSR;
  }

  public void setBboxSR(ISpatialReference bboxSR) {
    this.bboxSR = bboxSR;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public Integer getDpi() {
    return dpi;
  }

  public void setDpi(Integer dpi) {
    this.dpi = dpi;
  }

  public Map getLayerDefs() {
    return layerDefs;
  }

  public void setLayerDefs(Map layerDefs) {
    this.layerDefs = layerDefs;
  }

  public String getLayers() {
    return layers;
  }

  public void setLayers(String layers) {
    this.layers = layers;
  }

  public boolean isTransparent() {
    return transparent;
  }

  public void setTransparent(boolean transparent) {
    this.transparent = transparent;
  }

  public Map getLayerTimeOptions() {
    return layerTimeOptions;
  }

  public void setLayerTimeOptions(Map layerTimeOptions) {
    this.layerTimeOptions = layerTimeOptions;
  }

  public Map getDynamicLayers() {
    return dynamicLayers;
  }

  public void setDynamicLayers(Map dynamicLayers) {
    this.dynamicLayers = dynamicLayers;
  }

  public String getGdbVersion() {
    return gdbVersion;
  }

  public void setGdbVersion(String gdbVersion) {
    this.gdbVersion = gdbVersion;
  }

  public Double getMapScale() {
    return mapScale;
  }

  public void setMapScale(Double mapScale) {
    this.mapScale = mapScale;
  }

  public Double getRotation() {
    return rotation;
  }

  public void setRotation(Double rotation) {
    this.rotation = rotation;
  }

  public List<Map> getDatumTransformations() {
    return datumTransformations;
  }

  public void setDatumTransformations(List<Map> datumTransformations) {
    this.datumTransformations = datumTransformations;
  }

  public List<Map> getMapRangeValues() {
    return mapRangeValues;
  }

  public void setMapRangeValues(List<Map> mapRangeValues) {
    this.mapRangeValues = mapRangeValues;
  }

  public Map getLayerRangeValues() {
    return layerRangeValues;
  }

  public void setLayerRangeValues(Map layerRangeValues) {
    this.layerRangeValues = layerRangeValues;
  }

  public List<Map> getLayerParameterValues() {
    return layerParameterValues;
  }

  public void setLayerParameterValues(List<Map> layerParameterValues) {
    this.layerParameterValues = layerParameterValues;
  }
}
