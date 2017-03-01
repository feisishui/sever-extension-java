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

@SuppressWarnings("serial")
public abstract class AbstractDeserializer<T> extends StdDeserializer<T> {

    private ObjectMapper objectMapper;

    protected AbstractDeserializer(Class<T> vc, ObjectMapper objectMapper) {
        super(vc);
        this.objectMapper = objectMapper;
    }

    @Override
    public T deserialize(JsonParser jsonParser,
                         DeserializationContext deserializationContext) throws IOException,
            JsonProcessingException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode node = objectCodec.readTree(jsonParser);
        StringWriter writer = new StringWriter();
        this.objectMapper.writeValue(writer, node);
        writer.close();
        String json = writer.toString();
        T t = deserialize(json);
        return t;
    }

    protected abstract T deserialize(String json) throws IOException,
            JsonProcessingException;

}
