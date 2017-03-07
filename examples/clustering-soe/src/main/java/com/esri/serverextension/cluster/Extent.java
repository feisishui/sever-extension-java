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

/**
 * Immutable Extent
 */
public class Extent {
    private double _xmin;
    private double _ymin;
    private double _xmax;
    private double _ymax;

    /**
     * Construct an extent.  This is where everything gets assigned
     *
     * @param xmin
     * @param ymin
     * @param xmax
     * @param ymax
     */
    public Extent(double xmin, double ymin, double xmax, double ymax) {
        _xmin = xmin;
        _ymin = ymin;
        _xmax = xmax;
        _ymax = ymax;
    }

    public double getWidth() {
        return _xmax - _xmin;
    }

    public double getHeight() {
        return _ymax - _ymin;
    }

    public double getXMin() {
        return _xmin;
    }

    public double getYMin() {
        return _ymin;
    }

    public double getXMax() {
        return _xmax;
    }

    public double getYMax() {
        return _ymax;
    }
}
