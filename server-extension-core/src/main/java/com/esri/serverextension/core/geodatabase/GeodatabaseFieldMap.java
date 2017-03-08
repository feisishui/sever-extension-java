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

package com.esri.serverextension.core.geodatabase;

import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IField;
import com.esri.arcgis.geodatabase.IFields;
import com.esri.arcgis.geodatabase.IQueryFilter;
import com.esri.serverextension.core.util.ArcObjectsInteropException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

public class GeodatabaseFieldMap {

    private Map<String, FieldIndex> fieldsIndicesByName;

    public GeodatabaseFieldMap() {

    }

    public Collection<FieldIndex> getFieldIndices() {
        return Collections.unmodifiableCollection(fieldsIndicesByName.values());
    }

    public Set<String> getFieldNames() {
        return Collections.unmodifiableSet(fieldsIndicesByName.keySet());
    }

    public FieldIndex get(String fieldName) {
        return fieldsIndicesByName.get(fieldName);
    }

    public void initialize(IFields fields, String subFields) {
        try {
            String[] subFieldsArr = null;
            if (StringUtils.isNotEmpty(subFields) || !"*".equals(subFields)) {
                subFieldsArr = subFields.split(",");
            }
            int fieldCount = fields.getFieldCount();
            fieldsIndicesByName = new LinkedHashMap<>();
            for (int i = 0; i < fieldCount; i++) {
                if (subFieldsArr != null) {
                    for (String subField : subFieldsArr) {
                        if (subField.equals(fields.getField(i).getName())) {
                            fieldsIndicesByName.put(
                                    fields.getField(i).getName(),
                                    new FieldIndex(
                                            fields.getField(i),
                                            i
                                    )
                            );
                            break;
                        }
                    }
                } else {
                    fieldsIndicesByName.put(
                            fields.getField(i).getName(),
                            new FieldIndex(
                                    fields.getField(i),
                                    i
                            )
                    );
                }
            }
        } catch (IOException ex) {
            throw new ArcObjectsInteropException("Failed to generate field index map.");
        }
    }

    public static class FieldIndex {
        private IField field;
        private int index;

        public FieldIndex(IField field, int index) {
            this.field = field;
            this.index = index;
        }

        public IField getField() {
            return field;
        }

        public int getIndex() {
            return index;
        }
    }
}
