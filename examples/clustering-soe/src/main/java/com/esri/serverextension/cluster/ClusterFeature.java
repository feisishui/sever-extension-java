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
 * Created by kcoffin on 2/8/17.
 */
public class ClusterFeature {

    private double _value;
    private ClusterPoint _point;

    public ClusterFeature(ClusterPoint point, double value){
        _point = point;
        _value = value;
    }

    public double getValue(){
        return _value;
    }


    public ClusterPoint getPoint(){
        return _point;
    }

    public void print(){
        System.out.print("ClusterFeature  value:"+_value+"  ");
        _point.print();
        System.out.println();
    }
}
