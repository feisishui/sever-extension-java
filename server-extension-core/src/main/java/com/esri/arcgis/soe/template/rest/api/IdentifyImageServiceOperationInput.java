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

import com.esri.arcgis.geometry.IPoint;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by kyunam on 1/13/2017.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentifyImageServiceOperationInput extends OperationInput {

    private static final long serialVersionUID = 1L;

    private IPoint geometry;
    private String geometryType;
    private MosaicRule mosaicRule;
    private RenderingRule renderingRule;
    private List<RenderingRule> renderingRules;
    private IPoint pixelSize;
    private String time;
    @JsonProperty("returnGeometry")
    private Boolean returnGeometry;
    @JsonProperty("returnCatalogItems")
    private Boolean returnCatalogItems;

    public IPoint getGeometry() {
        return geometry;
    }

    public void setGeometry(IPoint geometry) {
        this.geometry = geometry;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
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

    public List<RenderingRule> getRenderingRules() {
        return renderingRules;
    }

    public void setRenderingRules(List<RenderingRule> renderingRules) {
        this.renderingRules = renderingRules;
    }

    public IPoint getPixelSize() {
        return pixelSize;
    }

    public void setPixelSize(IPoint pixelSize) {
        this.pixelSize = pixelSize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getReturnGeometry() {
        return returnGeometry;
    }

    public void setReturnGeometry(Boolean returnGeometry) {
        this.returnGeometry = returnGeometry;
    }

    public Boolean getReturnCatalogItems() {
        return returnCatalogItems;
    }

    public void setReturnCatalogItems(Boolean returnCatalogItems) {
        this.returnCatalogItems = returnCatalogItems;
    }
}
