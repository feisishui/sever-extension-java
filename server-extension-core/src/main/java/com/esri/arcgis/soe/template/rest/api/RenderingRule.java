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

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Map;

/**
 * kyunam_kim@esri.com on 1/9/17.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenderingRule implements Serializable {

  private static final long serialVersionUID = 1L;

  private String rasterFunction;

  private Map rasterFunctionArguments;

  private String outputPixelType;

  private String variableName;

  public String getRasterFunction() {
    return rasterFunction;
  }

  public void setRasterFunction(String rasterFunction) {
    this.rasterFunction = rasterFunction;
  }

  public Map getRasterFunctionArguments() {
    return rasterFunctionArguments;
  }

  public void setRasterFunctionArguments(Map rasterFunctionArguments) {
    this.rasterFunctionArguments = rasterFunctionArguments;
  }

  public String getOutputPixelType() {
    return outputPixelType;
  }

  public void setOutputPixelType(String outputPixelType) {
    this.outputPixelType = outputPixelType;
  }

  public String getVariableName() {
    return variableName;
  }

  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }
}
