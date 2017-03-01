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

/**
 * kyunam_kim@esri.com on 1/5/17.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transformation implements Serializable {

  private static final long serialVersionUID = 1L;

  private String wkid;
  @JsonProperty("transformForward")
  private boolean transformForward;

  public String getWkid() {
    return wkid;
  }

  public void setWkid(String wkid) {
    this.wkid = wkid;
  }

  public boolean isTransformForward() {
    return transformForward;
  }

  public void setTransformForward(boolean transformForward) {
    this.transformForward = transformForward;
  }
}
