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
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Statistic implements Serializable {

    private static final long serialVersionUID = 1L;

    private String statisticType;
    private String onStatisticField;
    private String outStatisticFieldName;

    public Statistic() {
    }

    public Statistic(String statisticType, String onStatisticField) {
        super();
        this.statisticType = statisticType;
        this.onStatisticField = onStatisticField;
    }

    public String getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(String statisticType) {
        this.statisticType = statisticType;
    }

    public String getOnStatisticField() {
        return onStatisticField;
    }

    public void setOnStatisticField(String onStatisticField) {
        this.onStatisticField = onStatisticField;
    }

    public String getOutStatisticFieldName() {
        return outStatisticFieldName;
    }

    public void setOutStatisticFieldName(String outStatisticFieldName) {
        this.outStatisticFieldName = outStatisticFieldName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
