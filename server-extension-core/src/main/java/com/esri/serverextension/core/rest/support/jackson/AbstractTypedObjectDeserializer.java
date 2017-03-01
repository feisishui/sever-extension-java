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
import java.util.Map;

@SuppressWarnings("serial")
public abstract class AbstractTypedObjectDeserializer<T> extends
        StdDeserializer<T> {
    private Map<String, Class<? extends T>> registry = new HashMap<String, Class<? extends T>>();
    private ObjectMapper objectMapper;

    protected AbstractTypedObjectDeserializer(Class<T> vc,
                                              ObjectMapper objectMapper) {
        super(vc);
        this.objectMapper = objectMapper;
    }

    protected void addTypeMapping(String typeName, Class<? extends T> clazz) {
        registry.put(typeName, clazz);
    }

    @Override
    public T deserialize(JsonParser jsonParser,
                         DeserializationContext deserializationContext) throws IOException,
            JsonProcessingException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode node = objectCodec.readTree(jsonParser);
        Class<? extends T> typeClass = null;
        if (node.isObject() && node.has("type")) {
            JsonNode type = node.get("type");
            if (type != null && type.isTextual()) {
                typeClass = registry.get(type.textValue());
            }
        }

        if (typeClass == null) {
            return null;
        }

        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, node);
        writer.close();
        String json = writer.toString();
        return objectMapper.readValue(json, typeClass);
    }
}
