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

import java.util.ArrayList;

/**
 * Created by kcoffin on 2/8/17.
 */
public class Cluster {

    private double _clusterCount;
    private Point  _point;
    private ArrayList<Feature> _features;

    //A cluster needs to be created with at least one feature
    public Cluster(Feature feature){
        _features = new ArrayList<>();
        _point = new Point(feature.getPoint());
        _clusterCount = feature.getValue();
        _features.add(feature);
    }

    public Point getPoint(){
        return _point;
    }

    public void setClusterCount(double ct){
        _clusterCount = ct;
    }

    public void addFeature(Feature feature){
        double value = feature.getValue();
        double count = _clusterCount;
        _features.add(feature);
        double ptc = value/(count + value);
        double ctc = count/(count + value);
        Point cluster = _point;
        Point p = feature.getPoint();

        double x = (p.x * ptc + (cluster.x * ctc));
        double y = (p.y * ptc + (cluster.y * ctc));
        cluster.x = x;
        cluster.y = y;
        _clusterCount += value;
    }

    public void addPointCluster(Feature feature, double ptCount){
        double count, x, y;
        count = getValue();

        Point p = feature.getPoint();
        getFeatures().add(feature);

        double ptc = ptCount/(count + ptCount);
        double ctc = count/(count + ptCount);

        x = (p.x * ptc + (_point.x * ctc));
        y = (p.y * ptc + (_point.y * ctc));
        _clusterCount += ptCount;
        _point.x = x;
        _point.y = y;
    }

    public double getValue(){
        return _clusterCount;
    }

    public ArrayList<Feature> getFeatures(){
        return _features;
    }





    public void print(){
        System.out.print("Cluster  value:"+_clusterCount+"  ");
        _point.print();
        System.out.println();
        for(Feature f:_features){
            f.print();
        }
        System.out.println("==============");
    }

}
