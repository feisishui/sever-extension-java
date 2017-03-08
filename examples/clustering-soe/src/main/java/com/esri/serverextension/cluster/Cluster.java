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

import com.esri.serverextension.core.rest.api.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kcoffin on 2/8/17.
 */
public class Cluster {

    private double value;
    private ClusterPoint point;
    private List<Feature> features;

    //A cluster needs to be created with at least one feature
    public Cluster(Feature feature, double featureValue) {
        features = new ArrayList<>();
        point = new ClusterPoint(feature.getGeometry());
        value = featureValue;
        features.add(feature);
    }

    public ClusterPoint getPoint() {
        return point;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double ct) {
        value = ct;
    }

    public void addFeature(Feature feature, double featureValue) {
        features.add(feature);
        double ptc = featureValue / (value + featureValue);
        double ctc = value / (value + featureValue);
        ClusterPoint cluster = point;
        ClusterPoint p = new ClusterPoint(feature.getGeometry());

        double x = (p.x * ptc + (cluster.x * ctc));
        double y = (p.y * ptc + (cluster.y * ctc));
        cluster.x = x;
        cluster.y = y;
        this.value += featureValue;
    }

    public void addPointCluster(Feature feature, double featureValue) {
        double x, y;

        ClusterPoint p = new ClusterPoint(feature.getGeometry());
        getFeatures().add(feature);

        double ptc = featureValue / (value + featureValue);
        double ctc = value / (value + featureValue);

        x = (p.x * ptc + (point.x * ctc));
        y = (p.y * ptc + (point.y * ctc));
        value += featureValue;
        point.x = x;
        point.y = y;
    }

    public List<Feature> getFeatures() {
        return features;
    }

}
