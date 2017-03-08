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

import com.esri.arcgis.geodatabase.IQueryFilter;
import com.esri.arcgis.geodatabase.ISpatialFilter;
import com.esri.arcgis.geodatabase.SpatialFilter;
import com.esri.arcgis.geometry.esriSpatialRelationEnum;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.serverextension.core.util.ArcObjectsInteropException;
import com.esri.serverextension.core.util.GenericEsriEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryMapServiceLayerOperationInput extends OperationInput {

  private static final long serialVersionUID = 1L;

  private String where;
  private String objectIds;
  private String time;
  private IGeometry geometry;
  private GeometryType geometryType;
  private Short geometryPrecision;
  private ISpatialReference inSR;
  private SpatialRelationship spatialRel;
  private String outFields;
  @JsonProperty("returnGeometry")
  private Boolean returnGeometry;
  private ISpatialReference outSR;
  @JsonProperty("returnIdsOnly")
  private Boolean returnIdsOnly;
  @JsonProperty("returnCountOnly")
  private Boolean returnCountOnly;
  private String orderByFields;
  private String groupByFieldsForStatistics;
  private List<Statistic> outStatistics;
  @JsonProperty("returnDistinctValues")
  private Boolean returnDistinctValues;
  private Integer resultOffset;
  private Integer resultRecordCount;
  private Double maxAllowableOffset;
  @JsonProperty("returnTrueCurves")
  private Boolean returnTrueCurves;
  private String relationParam;
  private String text;
  private Double distance;
  private UnitType units;
  @JsonProperty("returnExtentOnly")
  private Boolean returnExtentOnly;
  @JsonProperty("returnZ")
  private Boolean returnZ;
  @JsonProperty("returnM")
  private Boolean returnM;
  private String gdbVersion;
  private DatumTransformation datumTransformation; // TODO kyun4731 Test - Accepts only JSON
  private List<RangeValue> rangeValues; // TODO kyun4731 Test RangeValue.value which is the type of Object.
  private Map<String, Object> parameterValues; // TODO kyun4731 Test

  public QueryMapServiceLayerOperationInput() {
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Double getDistance() {
    return distance;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
  }

  public UnitType getUnits() {
    return units;
  }

  public void setUnits(UnitType units) {
    this.units = units;
  }

  public Boolean getReturnExtentOnly() {
    return returnExtentOnly;
  }

  public void setReturnExtentOnly(Boolean returnExtentOnly) {
    this.returnExtentOnly = returnExtentOnly;
  }

  public Boolean getReturnZ() {
    return returnZ;
  }

  public void setReturnZ(Boolean returnZ) {
    this.returnZ = returnZ;
  }

  public Boolean getReturnM() {
    return returnM;
  }

  public void setReturnM(Boolean returnM) {
    this.returnM = returnM;
  }

  public String getGdbVersion() {
    return gdbVersion;
  }

  public void setGdbVersion(String gdbVersion) {
    this.gdbVersion = gdbVersion;
  }

  public DatumTransformation getDatumTransformation() {
    return datumTransformation;
  }

  public void setDatumTransformation(DatumTransformation datumTransformation) {
    this.datumTransformation = datumTransformation;
  }

  public List<RangeValue> getRangeValues() {
    return rangeValues;
  }

  public void setRangeValues(List<RangeValue> rangeValues) {
    this.rangeValues = rangeValues;
  }

  public Map<String, Object> getParameterValues() {
    return parameterValues;
  }

  public void setParameterValues(Map<String, Object> parameterValues) {
    this.parameterValues = parameterValues;
  }

  public String getWhere() {
    return where;
  }

  public void setWhere(String where) {
    this.where = where;
  }

  public String getObjectIds() {
    return objectIds;
  }

  public void setObjectIds(String objectIds) {
    this.objectIds = objectIds;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public IGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(IGeometry geometry) {
    this.geometry = geometry;
  }

  public GeometryType getGeometryType() {
    return geometryType;
  }

  public void setGeometryType(GeometryType geometryType) {
    this.geometryType = geometryType;
  }

  public Short getGeometryPrecision() {
    return geometryPrecision;
  }

  public void setGeometryPrecision(Short geometryPrecision) {
    this.geometryPrecision = geometryPrecision;
  }

  public ISpatialReference getInSR() {
    return inSR;
  }

  public void setInSR(ISpatialReference inSR) {
    this.inSR = inSR;
  }

  public SpatialRelationship getSpatialRel() {
    return spatialRel;
  }

  public void setSpatialRel(SpatialRelationship spatialRel) {
    this.spatialRel = spatialRel;
  }

  public String getOutFields() {
    return outFields;
  }

  public void setOutFields(String outFields) {
    this.outFields = outFields;
  }

  public Boolean getReturnGeometry() {
    return returnGeometry;
  }

  public void setReturnGeometry(Boolean returnGeometry) {
    this.returnGeometry = returnGeometry;
  }

  public ISpatialReference getOutSR() {
    return outSR;
  }

  public void setOutSR(ISpatialReference outSR) {
    this.outSR = outSR;
  }

  public Boolean getReturnIdsOnly() {
    return returnIdsOnly;
  }

  public void setReturnIdsOnly(Boolean returnIdsOnly) {
    this.returnIdsOnly = returnIdsOnly;
  }

  public Boolean getReturnCountOnly() {
    return returnCountOnly;
  }

  public void setReturnCountOnly(Boolean returnCountOnly) {
    this.returnCountOnly = returnCountOnly;
  }

  public String getOrderByFields() {
    return orderByFields;
  }

  public void setOrderByFields(String orderByFields) {
    this.orderByFields = orderByFields;
  }

  public String getGroupByFieldsForStatistics() {
    return groupByFieldsForStatistics;
  }

  public void setGroupByFieldsForStatistics(String groupByFieldsForStatistics) {
    this.groupByFieldsForStatistics = groupByFieldsForStatistics;
  }

  public List<Statistic> getOutStatistics() {
    return outStatistics;
  }

  public void setOutStatistics(List<Statistic> outStatistics) {
    this.outStatistics = outStatistics;
  }

  public Boolean getReturnDistinctValues() {
    return returnDistinctValues;
  }

  public void setReturnDistinctValues(Boolean returnDistinctValues) {
    this.returnDistinctValues = returnDistinctValues;
  }

  public Integer getResultOffset() {
    return resultOffset;
  }

  public void setResultOffset(Integer resultOffset) {
    this.resultOffset = resultOffset;
  }

  public Integer getResultRecordCount() {
    return resultRecordCount;
  }

  public void setResultRecordCount(Integer resultRecordCount) {
    this.resultRecordCount = resultRecordCount;
  }

  public Double getMaxAllowableOffset() {
    return maxAllowableOffset;
  }

  public void setMaxAllowableOffset(Double maxAllowableOffset) {
    this.maxAllowableOffset = maxAllowableOffset;
  }

  public Boolean getReturnTrueCurves() {
    return returnTrueCurves;
  }

  public void setReturnTrueCurves(Boolean returnTrueCurves) {
    this.returnTrueCurves = returnTrueCurves;
  }

  public String getRelationParam() {
    return relationParam;
  }

  public void setRelationParam(String relationParam) {
    this.relationParam = relationParam;
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
