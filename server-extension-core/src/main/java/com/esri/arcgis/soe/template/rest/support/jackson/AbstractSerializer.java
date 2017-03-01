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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

@SuppressWarnings("serial")
public abstract class AbstractSerializer<T> extends StdSerializer<T> {

    private ObjectMapper objectMapper;

    protected AbstractSerializer(ObjectMapper objectMapper, Class<T> t) {
        super(t);
        this.objectMapper = objectMapper;
    }

    @Override
    public void serialize(T value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException,
            JsonGenerationException {
        String content = serialize(value);
        JsonNode node = this.objectMapper.readTree(content);
        jgen.writeTree(node);
    }

    protected abstract String serialize(T value) throws IOException,
            JsonGenerationException;

}
