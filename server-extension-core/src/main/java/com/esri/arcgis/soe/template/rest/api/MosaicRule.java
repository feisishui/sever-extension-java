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

package com.esri.arcgis.soe.template.rest.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * kyunam_kim@esri.com on 1/9/17.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class MosaicRule implements Serializable {

  private static final long serialVersionUID = 1L;

  private String mosaicMethod;
  private String where;
  private String sortField;
  private String sortValue;

  @JsonProperty("ascending")
  private boolean ascending;

  private List<Integer> lockRasterIds;
  private Map viewpoint;
  private List<Integer> fids;
  private String mosaicOperation;
  private RenderingRule itemRenderingRule;
  private String[] multidimensionalDefinition;

  public String getMosaicMethod() {
    return mosaicMethod;
  }

  public void setMosaicMethod(String mosaicMethod) {
    this.mosaicMethod = mosaicMethod;
  }

  public String getWhere() {
    return where;
  }

  public void setWhere(String where) {
    this.where = where;
  }

  public String getSortField() {
    return sortField;
  }

  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  public String getSortValue() {
    return sortValue;
  }

  public void setSortValue(String sortValue) {
    this.sortValue = sortValue;
  }

  public boolean isAscending() {
    return ascending;
  }

  public void setAscending(boolean ascending) {
    this.ascending = ascending;
  }

  public List<Integer> getLockRasterIds() {
    return lockRasterIds;
  }

  public void setLockRasterIds(List<Integer> lockRasterIds) {
    this.lockRasterIds = lockRasterIds;
  }

  public Map getViewpoint() {
    return viewpoint;
  }

  public void setViewpoint(Map viewpoint) {
    this.viewpoint = viewpoint;
  }

  public List<Integer> getFids() {
    return fids;
  }

  public void setFids(List<Integer> fids) {
    this.fids = fids;
  }

  public String getMosaicOperation() {
    return mosaicOperation;
  }

  public void setMosaicOperation(String mosaicOperation) {
    this.mosaicOperation = mosaicOperation;
  }

  public RenderingRule getItemRenderingRule() {
    return itemRenderingRule;
  }

  public void setItemRenderingRule(RenderingRule itemRenderingRule) {
    this.itemRenderingRule = itemRenderingRule;
  }

  public String[] getMultidimensionalDefinition() {
    return multidimensionalDefinition;
  }

  public void setMultidimensionalDefinition(String[] multidimensionalDefinition) {
    this.multidimensionalDefinition = multidimensionalDefinition;
  }
}
