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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SuppressWarnings("serial")
public class ArcGISTypesModule extends SimpleModule {

    private ObjectMapper objectMapper;

    public ArcGISTypesModule() {
        super("ArcObjectsModule");
    }

    @Override
    public void setupModule(SetupContext context) {
        this.objectMapper = context.getOwner();

        addDeserializer(IGeometry.class, new GeometryDeserializer(
                this.objectMapper));
        addDeserializer(IPoint.class, new PointDeserializer(this.objectMapper));
        addDeserializer(IMultipoint.class, new MultipointDeserializer(
                this.objectMapper));
        addDeserializer(IPolyline.class, new PolylineDeserializer(
                this.objectMapper));
        addDeserializer(IPolygon.class, new PolygonDeserializer(
                this.objectMapper));
        addDeserializer(IEnvelope.class, new EnvelopeDeserializer(
                this.objectMapper));
        addSerializer(IGeometry.class,
                new GeometrySerializer(this.objectMapper));

        addDeserializer(ISpatialReference.class,
                new SpatialReferenceDeserializer(this.objectMapper));
        addSerializer(ISpatialReference.class, new SpatialReferenceSerializer(
                this.objectMapper));

        super.setupModule(context);
    }
}
