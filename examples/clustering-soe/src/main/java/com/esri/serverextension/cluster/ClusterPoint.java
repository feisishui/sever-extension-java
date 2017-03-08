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

package com.esri.serverextension.cluster;

import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IPoint;
import com.esri.arcgis.geometry.Point;
import com.esri.serverextension.core.util.ArcObjectsInteropException;

import java.io.IOException;

/**
 * Created by kcoffin on 2/8/17.
 */
public class ClusterPoint {
    public double x;
    public double y;

    public ClusterPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public ClusterPoint(ClusterPoint pt) {
        x = pt.x;
        y = pt.y;
    }

    public ClusterPoint(IGeometry geometry) {
        if (geometry == null) {
            throw new NullPointerException("Argument 'geometry' must not be null.");
        }
        if (geometry instanceof IPoint) {
            IPoint pt = (IPoint)geometry;
            try {
                x = pt.getX();
                y = pt.getY();
            } catch (IOException ex) {
                throw new ArcObjectsInteropException("Failed to create cluster point from IPoint.");
            }
        } else {
            throw new IllegalArgumentException(String.format("Geometry is not a point: %1$s", geometry.getClass().getName()));
        }
    }

    public double squareDistance(ClusterPoint pt) {
        double dx = pt.x - x;
        double dy = pt.y - y;
        return (dx * dx) + (dy * dy);
    }

    public double distance(ClusterPoint pt) {
        return Math.sqrt(squareDistance(pt));
    }

    public IPoint getPoint() {
        try {
            Point pt = new Point();
            pt.setX(x);
            pt.setY(y);
            return pt;
        } catch (IOException ex) {
            throw new ArcObjectsInteropException("Failed to create point.", ex);
        }
    }
}
