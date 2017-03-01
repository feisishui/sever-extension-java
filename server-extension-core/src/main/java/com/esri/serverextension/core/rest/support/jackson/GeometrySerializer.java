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

import com.esri.arcgis.geometry.IGeometry;
import com.esri.serverextension.core.rest.json.JSONGeometryMapper;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@SuppressWarnings("serial")
public class GeometrySerializer extends AbstractSerializer<IGeometry> {

    private JSONGeometryMapper geometryMapper = new JSONGeometryMapper();

    protected GeometrySerializer(ObjectMapper objectMapper) {
        super(objectMapper, IGeometry.class);
    }

    @Override
    protected String serialize(IGeometry value) throws IOException,
            JsonGenerationException {
        return this.geometryMapper.writeGeometry(value, false);
    }

}
