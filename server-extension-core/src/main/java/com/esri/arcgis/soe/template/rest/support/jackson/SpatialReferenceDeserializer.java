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

package com.esri.arcgis.soe.template.rest.support.jackson;

import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.soe.template.rest.json.JSONGeometryMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@SuppressWarnings("serial")
public class SpatialReferenceDeserializer extends
        AbstractDeserializer<ISpatialReference> {

    private JSONGeometryMapper geometryMapper = new JSONGeometryMapper();

    protected SpatialReferenceDeserializer(ObjectMapper objectMapper) {
        super(ISpatialReference.class, objectMapper);
    }

    @Override
    protected ISpatialReference deserialize(String json) throws IOException,
            JsonProcessingException {
        ISpatialReference spatialReference = this.geometryMapper
                .readSpatialReference(json);
        return spatialReference;
    }

}
