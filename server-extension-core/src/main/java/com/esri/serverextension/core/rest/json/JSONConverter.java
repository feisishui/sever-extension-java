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

package com.esri.serverextension.core.rest.json;

import com.esri.arcgis.server.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class JSONConverter {

    private JSONConverter() {
    }

    public static byte[] toByteArray(JSONObject jsonObj) {
        if (jsonObj == null) {
            return null;
        }
        try {
            return jsonObj.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
            throw new JSONException(ex);
        }
    }

    public static byte[] toByteArray(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        try {
            return jsonStr.getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
            throw new JSONException(ex);
        }
    }

    public static byte[] toByteArray(Resource jsonRes) {
        if (jsonRes == null) {
            return null;
        }
        try {
            return IOUtils.toByteArray(jsonRes.getInputStream());
        } catch (IOException ex) {
            throw new JSONException(ex);
        }
    }

    public static JSONObject toJSONObject(byte[] data) {
        if (data == null) {
            return null;
        }
        return new JSONObject(toString(data));
    }

    public static JSONObject toJSONObject(Resource jsonRes) {
        if (jsonRes == null) {
            return null;
        }
        try {
            return new JSONObject(IOUtils.toString(jsonRes.getInputStream(),
                    "utf-8"));
        } catch (IOException ex) {
            throw new JSONException(ex);
        }
    }

    public static String toString(byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return new String(data, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            throw new JSONException(ex);
        }
    }

    public static String toString(Resource jsonRes) {
        if (jsonRes == null) {
            return null;
        }
        try {
            return IOUtils.toString(jsonRes.getInputStream(), "utf-8");
        } catch (IOException ex) {
            throw new JSONException(ex);
        }
    }

}
