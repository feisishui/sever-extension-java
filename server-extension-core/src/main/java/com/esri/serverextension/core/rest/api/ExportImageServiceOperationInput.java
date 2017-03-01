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

/**
 * kyunam_kim@esri.com on 1/9/17.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExportImageServiceOperationInput extends OperationInput {

  private static final long serialVersionUID = 1L;

  private String bbox;
  private String size;
  private ISpatialReference imageSR;
  private ISpatialReference bboxSR;
  private String format;
  private String time;
  @JsonProperty("adjustAspectRatio")
  private boolean adjustAspectRatio;
  private Integer lercVersion;
  private String pixelType;
  private String noData;
  private String noDataInterpretation;
  private String interpolation;
  private String compression;
  private Double compressionQuality;
  private Double compressionTolerance;
  private String bandIds;
  private MosaicRule mosaicRule;
  private RenderingRule renderingRule;

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

  public boolean isAdjustAspectRatio() {
    return adjustAspectRatio;
  }

  public void setAdjustAspectRatio(boolean adjustAspectRatio) {
    this.adjustAspectRatio = adjustAspectRatio;
  }

  public Integer getLercVersion() {
    return lercVersion;
  }

  public void setLercVersion(Integer lercVersion) {
    this.lercVersion = lercVersion;
  }

  public String getPixelType() {
    return pixelType;
  }

  public void setPixelType(String pixelType) {
    this.pixelType = pixelType;
  }

  public String getNoData() {
    return noData;
  }

  public void setNoData(String noData) {
    this.noData = noData;
  }

  public String getNoDataInterpretation() {
    return noDataInterpretation;
  }

  public void setNoDataInterpretation(String noDataInterpretation) {
    this.noDataInterpretation = noDataInterpretation;
  }

  public String getInterpolation() {
    return interpolation;
  }

  public void setInterpolation(String interpolation) {
    this.interpolation = interpolation;
  }

  public String getCompression() {
    return compression;
  }

  public void setCompression(String compression) {
    this.compression = compression;
  }

  public Double getCompressionQuality() {
    return compressionQuality;
  }

  public void setCompressionQuality(Double compressionQuality) {
    this.compressionQuality = compressionQuality;
  }

  public Double getCompressionTolerance() {
    return compressionTolerance;
  }

  public void setCompressionTolerance(Double compressionTolerance) {
    this.compressionTolerance = compressionTolerance;
  }

  public String getBandIds() {
    return bandIds;
  }

  public void setBandIds(String bandIds) {
    this.bandIds = bandIds;
  }

  public MosaicRule getMosaicRule() {
    return mosaicRule;
  }

  public void setMosaicRule(MosaicRule mosaicRule) {
    this.mosaicRule = mosaicRule;
  }

  public RenderingRule getRenderingRule() {
    return renderingRule;
  }

  public void setRenderingRule(RenderingRule renderingRule) {
    this.renderingRule = renderingRule;
  }
}
