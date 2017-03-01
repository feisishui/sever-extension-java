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

import com.esri.arcgis.geometry.*;
import com.esri.serverextension.core.rest.json.JSONGeometryMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class GeometryDeserializer extends StdDeserializer<IGeometry> {

    private Map<String, Class<? extends IGeometry>> registry = new HashMap<String, Class<? extends IGeometry>>();
    private ObjectMapper objectMapper;
    private JSONGeometryMapper geometryMapper = new JSONGeometryMapper();

    protected GeometryDeserializer(ObjectMapper objectMapper) {
        super(IGeometry.class);
        this.objectMapper = objectMapper;
        registry.put("x", IPoint.class);
        registry.put("points", IMultipoint.class);
        registry.put("paths", IPolyline.class);
        registry.put("rings", IPolygon.class);
        registry.put("xmin", IEnvelope.class);
    }

    @Override
    public IGeometry deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) throws IOException,
            JsonProcessingException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode node = objectCodec.readTree(jsonParser);
        Class<? extends IGeometry> geometryClass = null;
        if (node.isObject()) {
            Iterator<Entry<String, JsonNode>> elementsIterator = node.fields();
            while (elementsIterator.hasNext()) {
                Entry<String, JsonNode> element = elementsIterator.next();
                String name = element.getKey();
                if (registry.containsKey(name)) {
                    geometryClass = registry.get(name);
                    break;
                }
            }
        } else if (node.isTextual()) {
            String text = node.textValue();
            if (this.geometryMapper.isSimplePointFormat(text)) {
                return this.geometryMapper.readPoint(text);
            } else if (this.geometryMapper.isSimpleEnvelopeFormat(text)) {
                return this.geometryMapper.readEnvelope(text);
            }
        }

        if (geometryClass == null) {
            return null;
        }

        StringWriter writer = new StringWriter();
        this.objectMapper.writeValue(writer, node);
        writer.close();
        String json = writer.toString();
        return this.objectMapper.readValue(json, geometryClass);
    }
}
