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
public class Point {
    public double x;
    public double y;
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Point(Point pt){
        x = pt.x;
        y = pt.y;
    }

    public double squareDistance(Point pt){
        double dx = pt.x - x;
        double dy = pt.y - y;
        return (dx*dx) + (dy*dy);
    }
    public double distance(Point pt){
        return Math.sqrt(squareDistance(pt));
    }



    public void print(){
        System.out.print("("+x+","+y+")");

    }
}
