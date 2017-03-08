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

package com.esri.serverextension.core.util;

import com.esri.serverextension.core.rest.json.JSONException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class GenericEsriEnum {

    private GenericEsriEnum() {
    }

    /**
     * Deserializer for Esri-style enums, which are interfaces with public static
     * int members, e.g. com.esri.arcgis.geometry.esriGeometryType. This method converts
     * the string representation of one of the fields, e.g. esriGeometryPoint, to
     * its equivalent int representation, e.g. 1, given the specified Esri-style enum class.
     *
     * @param esriEnum The Esri-style enum class used to deserialize a string value.
     * @param enumValue The name of a public static integer field in the Esri-style enum class.
     * @param <T> The Esri-style enum class used to deserialize a string value.
     * @return the integer value for the specified string
     */
    public static final <T> Integer valueOf(Class<T> esriEnum, String enumValue) {
        Field[] fields = esriEnum.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (!Modifier.isPublic(field.getModifiers())) {
                continue;
            }
            if (!field.getType().equals(Integer.TYPE)) {
                continue;
            }
            if (field.getName().equals(enumValue)) {
                try {
                    return field.getInt(null);
                } catch (IllegalAccessException ex) {
                    throw new JSONException("Cannot access property.", ex);
                }
            }
        }
        throw new IllegalArgumentException(String.format(
                "Cannot find field %1$s in class %2$s", enumValue, esriEnum.getName()));
    }
}
