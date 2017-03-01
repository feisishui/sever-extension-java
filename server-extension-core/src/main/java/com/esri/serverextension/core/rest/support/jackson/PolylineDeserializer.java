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

package com.esri.serverextension.core.rest.support.jackson;

import com.esri.arcgis.geometry.IPolyline;
import com.esri.serverextension.core.rest.json.JSONGeometryMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@SuppressWarnings("serial")
public class PolylineDeserializer extends AbstractDeserializer<IPolyline> {

    private JSONGeometryMapper geometryMapper = new JSONGeometryMapper();

    protected PolylineDeserializer(ObjectMapper objectMapper) {
        super(IPolyline.class, objectMapper);
    }

    @Override
    protected IPolyline deserialize(String json) throws IOException,
            JsonProcessingException {
        return this.geometryMapper.readPolyline(json);
    }

}
