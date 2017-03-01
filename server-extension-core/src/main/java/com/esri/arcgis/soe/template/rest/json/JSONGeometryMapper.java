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

package com.esri.arcgis.soe.template.rest.json;

import com.esri.arcgis.geometry.*;
import com.esri.arcgis.soe.template.geometry.SpatialReferenceFactory;
import com.esri.arcgis.system.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONGeometryMapper {

    public static final String SIMPLE_POINT_FORMAT_REGEX = "\\w*(?<x>[-+]?([0-9]+(\\.[0-9]*)?|\\.[0-9]+))\\w*,"
            + "\\w*?(?<y>[-+]?([0-9]+(\\.[0-9]*)?|\\.[0-9]+))\\w*";

    public static final String SIMPLE_ENVELOPE_FORMAT_REGEX = "\\w*(?<xmin>[-+]?([0-9]+(\\.[0-9]*)?|\\.[0-9]+))\\w*,"
            + "\\w*?(?<ymin>[-+]?([0-9]+(\\.[0-9]*)?|\\.[0-9]+))\\w*,"
            + "\\w*?(?<xmax>[-+]?([0-9]+(\\.[0-9]*)?|\\.[0-9]+))\\w*,"
            + "\\w*?(?<ymax>[-+]?([0-9]+(\\.[0-9]*)?|\\.[0-9]+))\\w*";

    public JSONGeometryMapper() {
    }

    public boolean isSimplePointFormat(String geometry) {
        Pattern p = Pattern.compile(SIMPLE_POINT_FORMAT_REGEX);
        Matcher m = p.matcher(geometry);
        return m.matches();
    }

    public IPoint readPoint(String geometry) {
        Pattern p = Pattern.compile(SIMPLE_POINT_FORMAT_REGEX);
        Matcher m = p.matcher(geometry);
        if (m.matches()) {
            double x = Double.parseDouble(m.group("x"));
            double y = Double.parseDouble(m.group("y"));
            IPoint point;
            try {
                point = new Point();
                point.putCoords(x, y);
            } catch (IOException e) {
                throw new JSONException(String.format(
                        "Cannot map JSON string '%1$s' to %2$s", geometry,
                        "IPoint"), e);
            }
            return point;
        }

        try {
            IJSONConverterGeometry converterGeometry = new JSONConverterGeometry();
            IJSONReader jsonReader = createJSONReader(geometry);
            IPoint point = converterGeometry.readPoint(jsonReader);
            Cleaner.release(jsonReader);
            Cleaner.release(converterGeometry);
            return point;
        } catch (IOException e) {
            throw new JSONException(
                    String.format("Cannot map JSON string '%1$s' to %2$s",
                            geometry, "IPoint"), e);
        }
    }

    public IMultipoint readMultipoint(String geometry) {
        try {
            IJSONConverterGeometry converterGeometry = new JSONConverterGeometry();
            IJSONReader jsonReader = createJSONReader(geometry);
            IMultipoint multiPoint = converterGeometry.readMultipoint(
                    jsonReader, false, false);
            Cleaner.release(jsonReader);
            Cleaner.release(converterGeometry);
            return multiPoint;
        } catch (IOException e) {
            throw new JSONException(String.format(
                    "Cannot map JSON string '%1$s' to %2$s", geometry,
                    "IMultipoint"), e);
        }
    }

    public IPolyline readPolyline(String geometry) {
        try {
            IJSONConverterGeometry converterGeometry = new JSONConverterGeometry();
            IJSONReader jsonReader = createJSONReader(geometry);
            IPolyline polyline = converterGeometry.readPolyline(jsonReader,
                    false, false);
            Cleaner.release(jsonReader);
            Cleaner.release(converterGeometry);
            return polyline;
        } catch (IOException e) {
            throw new JSONException(String.format(
                    "Cannot map JSON string '%1$s' to %2$s", geometry,
                    "IPolyline"), e);
        }
    }

    public IPolygon readPolygon(String geometry) {
        try {
            IJSONConverterGeometry converterGeometry = new JSONConverterGeometry();
            IJSONReader jsonReader = createJSONReader(geometry);
            IPolygon polygon = converterGeometry.readPolygon(jsonReader, false,
                    false);
            Cleaner.release(jsonReader);
            Cleaner.release(converterGeometry);
            return polygon;
        } catch (IOException e) {
            throw new JSONException(String.format(
                    "Cannot convert JSON string '%1$s' to %2$s", geometry,
                    "IPolygon"), e);
        }
    }

    public boolean isSimpleEnvelopeFormat(String geometry) {
        Pattern p = Pattern.compile(SIMPLE_ENVELOPE_FORMAT_REGEX);
        Matcher m = p.matcher(geometry);
        return m.matches();
    }

    public IEnvelope readEnvelope(String geometry) {
        Pattern p = Pattern.compile(SIMPLE_ENVELOPE_FORMAT_REGEX);
        Matcher m = p.matcher(geometry);
        if (m.matches()) {
            double xmin = Double.parseDouble(m.group("xmin"));
            double ymin = Double.parseDouble(m.group("ymin"));
            double xmax = Double.parseDouble(m.group("xmax"));
            double ymax = Double.parseDouble(m.group("ymax"));
            IEnvelopeGEN envelope;
            try {
                envelope = new Envelope();
                envelope.setXMin(xmin);
                envelope.setYMin(ymin);
                envelope.setXMax(xmax);
                envelope.setYMax(ymax);
            } catch (IOException e) {
                throw new JSONException(String.format(
                        "Cannot map JSON string '%1$s' to %2$s", geometry,
                        "IEnvelopeGEN"), e);
            }
            return (IEnvelope) envelope;
        }

        try {
            IJSONConverterGeometry converterGeometry = new JSONConverterGeometry();
            IJSONReader jsonReader = createJSONReader(geometry);
            IEnvelope envelope = converterGeometry.readEnvelope(jsonReader);
            Cleaner.release(jsonReader);
            Cleaner.release(converterGeometry);
            return envelope;
        } catch (IOException e) {
            throw new JSONException(String.format(
                    "Cannot map JSON string '%1$s' to %2$s", geometry,
                    "IEnvelope"), e);
        }
    }

    public ISpatialReference readSpatialReference(String spatialReference) {
        try {
            if (StringUtils.isNumeric(spatialReference)) {
                SpatialReferenceFactory spatialReferenceFactory = new SpatialReferenceFactory();
                try {
                    int wkid = Integer.parseInt(spatialReference);
                    return spatialReferenceFactory.create(wkid);
                } catch (NumberFormatException ignored) {
                }
            }
            IJSONConverterGeometry converterGeometry = new JSONConverterGeometry();
            IJSONReader jsonReader = createJSONReader(spatialReference);
            ISpatialReference sr = converterGeometry
                    .readSpatialReference(jsonReader);
            Cleaner.release(jsonReader);
            Cleaner.release(converterGeometry);
            return sr;
        } catch (IOException e) {
            throw new JSONException(String.format(
                    "Cannot map JSON string '%1$s' to %2$s", spatialReference,
                    "ISpatialReference"), e);
        }
    }

    public String writeGeometry(IGeometry geometry, boolean skipSpatialReference) {
        try {
            IJSONObject jsonObject = new JSONObject();
            IJSONConverterGeometry converterGeometry = new JSONConverterGeometry();
            converterGeometry.queryJSONGeometry(geometry, skipSpatialReference, jsonObject);
            String json = jsonObject.toJSONString(null);
            Cleaner.release(converterGeometry);
            Cleaner.release(jsonObject);
            return json;
        } catch (IOException e) {
            throw new JSONException("Cannot map geometry to JSON string", e);
        }
    }

    public String writeSpatialReference(ISpatialReference spatialReference) {
        try {
            IJSONObject jsonObject = new JSONObject();
            IJSONConverterGeometry converterGeometry = new JSONConverterGeometry();
            converterGeometry.queryJSONSpatialReference(spatialReference,
                    jsonObject);
            String json = jsonObject.toJSONString(null);
            Cleaner.release(converterGeometry);
            Cleaner.release(jsonObject);
            return json;
        } catch (IOException e) {
            throw new JSONException(
                    "Cannot map spatial reference to JSON string", e);
        }
    }

    private IJSONReader createJSONReader(String json) {
        IJSONReader reader;
        try {
            reader = new JSONReader();
            reader.readFromString(json);
        } catch (IOException e) {
            throw new JSONException(String.format(
                    "Cannot read JSON string '%1$s'", json), e);
        }
        return reader;
    }
}
