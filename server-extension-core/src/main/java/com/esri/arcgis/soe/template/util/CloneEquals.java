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

package com.esri.arcgis.soe.template.util;

import com.esri.arcgis.system.IClone;

import java.io.IOException;

public class CloneEquals {

    public CloneEquals() {
    }

    public boolean equals(IClone o1, IClone o2) {
        if (o1 == null && o2 == null)
            return true;
        if (o1 == null || o2 == null)
            return false;
        if (o1 == o2)
            return true;
        try {
            return o1.isEqual(o2);
        } catch (IOException e) {
            throw new ArcObjectsInteropException(
                    "Exception when comparing IClone objects", e);
        }
    }

}
