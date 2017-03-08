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

import java.util.*;

/**
 * Assembles the clustering
 */
public class ClusterAssembler {

    //The grid's extent
    private ClusterExtent _extent;


    //cellsize of grid
    private double _cellSize;

    private double _mapUnitsPerPixel;

    //number of columns in grid
    private int    _numColumns;

    //number of columns in grid
    private int    _numRows;


    //the cells which keeps the clustering info
    private Map<Integer, ArrayList<Cluster>> _cells;

    //the desired cluster distance in pixels
    private double _clusterDistanceInPixels;

    //All the clusters which are created
    private ArrayList<Cluster> _clusters;


    /**
     * Assemble the clustering
     * @param features all the features
     * @param mapUnitsPerPixel map units per pixel (like meters per pixel)
     * @param clusterDistanceInPixels cluster distance in pixels
     * @param extent the extent in real world coordinates
     */
    public ClusterAssembler(ArrayList<ClusterFeature> features, double mapUnitsPerPixel,
                            double clusterDistanceInPixels, ClusterExtent extent){
        addAllFeatures(features, mapUnitsPerPixel, clusterDistanceInPixels, extent);
    }

    /**
     * Retrieves all the clusters
     * @return
     */
    public ArrayList<Cluster>getClusters(){
        return _clusters;
    }


    //Adds all the features. Called internally by the ctor
    private void addAllFeatures(ArrayList<ClusterFeature> features, double mapUnitsPerPixel,
                                double clusterDistanceInPixels, ClusterExtent extent
                               ){
        _cells = new HashMap<> ();
        _mapUnitsPerPixel = mapUnitsPerPixel;
        _clusterDistanceInPixels = clusterDistanceInPixels;
        _cellSize = mapUnitsPerPixel * _clusterDistanceInPixels;
        _extent = extent;
        _numColumns = getGridColumn(extent.getXMax())+1;
        _numRows = getGridRow(extent.getYMax())+1;
        _clusters = new ArrayList<>();

        //first sort the features based on the clusterfieldIndex
        // Sorting by Lambda
        Collections.sort(features, (ClusterFeature feature2, ClusterFeature feature1)->
                ((Double)feature1.getValue()).compareTo(feature2.getValue()));


        /* JAVA 1.7
        Collections.sort(features, new Comparator<ClusterFeature>() {
            @Override
            public int compare(ClusterFeature feature2, ClusterFeature feature1)
            {
                return  ((Double)feature1.getValue()).compareTo((Double)feature2.getValue());
            }
        });
        */

        for (ClusterFeature feature:features){
            addFeature(feature);
        }



        for (Cluster cluster:_clusters){
            fixCluster(cluster);
        }


        for (Cluster cluster:_clusters){
            fixCluster(cluster);
        }

/*
        System.out.println("+++++++++++++++++++++++++++++++++++++");
        for (Cluster cluster:_clusters){
            examineCluster(cluster);
        }
*/
/*
        for (Cluster cluster:_clusters){
            cluster.print();
        }
*/

    }




    /**
     * Add a feature
     * @param feature
     */
    private void addFeature(ClusterFeature feature) {
        Cluster closestCluster = getClosestCluster(feature.getPoint());

        if (closestCluster != null) {
            addFeatureToCluster(feature, closestCluster);
        }else{
            createCluster(feature);//create new cluster
        }

    }

    //from yValue real-world, what is the grid row
    private int getGridRow(double yValue){
        return (int) Math.floor((yValue-_extent.getYMin())/_cellSize);
    }

    //from xValue real-world, what is the grid column
    private int getGridColumn(double xValue){
        return (int) Math.floor((xValue-_extent.getXMin())/_cellSize);
    }

    //add a feature to an EXISTING cluster
    private void addFeatureToCluster(ClusterFeature feature, Cluster cluster) {
        //remove it from the grid because its coordinates are going to change
        removeClusterFromGrid(cluster);

        //add the feature to the cluster
        cluster.addFeature(feature);

        //add it back in to the grid
        addClusterToGrid(cluster);
    }

    //remove a cluster from the grid (it is already in the grid)
    private void removeClusterFromGrid(Cluster cluster){
        ClusterPoint pt = cluster.getPoint();
        int row = getGridRow(pt.y);
        int column = getGridColumn(pt.x);
        int index = _numRows * row + column;
        ArrayList<Cluster> cell = _cells.get(index);

        if (cell != null) {
            cell.remove(cluster);
        }else{
            System.out.println("Programming error");
        }
    }

    //Add the cluster to the grid
    private void addClusterToGrid(Cluster cluster){
        ClusterPoint pt = cluster.getPoint();
        int row = getGridRow(pt.y);
        int column = getGridColumn(pt.x);
        int index = _numRows * row + column;
        ArrayList<Cluster> cell = _cells.get(index);

        if (cell == null) {
            cell = new ArrayList<>();
            _cells.put(index, cell);
        }
        cell.add(cluster);
    }

    private void createCluster(ClusterFeature feature) {
        Cluster cluster = new Cluster(feature);
        addClusterToGrid(cluster);
        _clusters.add(cluster);
    }




    /**
     * Gets the closest cluster within the cell distance
     * @param pt
     * @return
     */
    public Cluster getClosestCluster(ClusterPoint pt){
        int row = getGridRow(pt.y);
        int column = getGridColumn(pt.x);

        //should never happen as all features should come from within the extent
        if (row < 0 || column < 0 || row>=_numRows || column>=_numColumns){
            System.out.println("There's an error in the query");
            return null;
        }

        int yStart = row;
        int yEnd = row;
        if (row > 0){
            yStart = row-1;
        }
        if (row < _numRows-1){
            yEnd = row+1;
        }


        int xStart = column;
        int xEnd = column;
        if (column > 0){
            xStart = column-1;
        }
        if (column < _numColumns-1){
            xEnd = column+1;
        }

        /*
        int xStart = (int)(Math.floor((extent.xmin - this._xmin) / this._cellSize));
        int xEnd = (int)(Math.floor((extent.xmax - this._xmin) / this._cellSize));
        int yStart = (int)(Math.floor((extent.ymin - this._ymin) / this._cellSize));
        int yEnd = (int)(Math.floor((extent.ymax - this._ymin) / this._cellSize));
        */

        Cluster minCluster = null;
        double minDis2 = Double.MAX_VALUE;

        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                int index = _numRows * y + x;
                ArrayList<Cluster> cell = _cells.get(index);
                if (cell != null) {
                    for (int i = 0; i < cell.size(); i++) {
                        Cluster cluster = cell.get(i);
                        double dis2 = pt.squareDistance(cluster.getPoint());
                        if (dis2 < minDis2) {
                            minDis2 = dis2;
                            minCluster = cluster;
                        }
                    }
                }
            }
        }
        if (minDis2 > 0){
            minDis2 = Math.sqrt(minDis2);
        }
        if (minDis2 > _cellSize){
            return null;
        }
        return minCluster;
    }




    //This examines a cluster and determines if all features in it are the closest to it.
    public void examineCluster(Cluster cluster){


        ArrayList<ClusterFeature> features = cluster.getFeatures();
        for (int k=0;k<features.size();k++){
            ClusterFeature feature = features.get(k);
            ClusterPoint p = feature.getPoint();
            Cluster clust = getClosestCluster(p);
            if (clust != cluster){
                double pixels = cluster.getPoint().distance(p)/_mapUnitsPerPixel;
                System.out.print("Not closest to cluster...pixel Distance="+Math.round(pixels));
                if (clust == null){
                    System.out.println();
                }else{
                    double closerPixels = clust.getPoint().distance(p)/_mapUnitsPerPixel;
                    System.out.println("   Closer to distance="+Math.round(closerPixels));
                }
            }
        }
    }




    public void fixCluster(Cluster cluster) {
        double distance = _cellSize;//this._mapUnitsPerPixel*this._clusterDistanceInPixels;
        double distanceSq = distance * distance;
        double test = distanceSq * 2.0;
        ArrayList<ClusterFeature> features = cluster.getFeatures();
        for (int k = features.size() - 1; k >= 0; k--) {
            ClusterFeature feature = features.get(k);
            ClusterPoint p = feature.getPoint();

            Cluster minPt = this.getClosestCluster(p);

            if (minPt != null && minPt != cluster) {
                double ptCount = feature.getValue();

                double count = cluster.getValue();

                //features.splice(k, 1);
                features.remove(k);

                double ptCountCount = count - ptCount;
                cluster.setClusterCount(ptCountCount);
                ClusterPoint clusterPoint = cluster.getPoint();
                if (ptCountCount > 0) {
                    double ptc = ptCount / ptCountCount;
                    double ctc = count / ptCountCount;


                    double xx = (clusterPoint.x * ctc) - (p.x * ptc);//x = (cluster.x*count-pt.x*ptCount)/(count-ptCount)
                    double yy = (clusterPoint.y * ctc) - (p.y * ptc);
                    //var xx = ((cluster.x*count)-(pt.x*ptCount))/(count);
                    //var yy = ((cluster.x*count)-(pt.x*ptCount))/(count);
                    clusterPoint.x = xx;
                    clusterPoint.y = yy;
                    this.addCells(cluster);

                } else {
                    clusterPoint.x = 0;
                    clusterPoint.y = 0;
                }


                //add to another
                //this._clusterAddPoint(feature, minPt, ptCount);
                minPt.addPointCluster(feature, ptCount);


            }

        }


    }

    public void addCellsForAllClusters(){
        //this._cells = [];
        _cells = new HashMap<> ();
        for (Cluster cluster:_clusters){
            addCells(cluster);
        }

    }

    public void addCells(Cluster cluster){
        ClusterPoint clusterPoint = cluster.getPoint();
        int row = getGridRow(clusterPoint.y);
        int column = getGridColumn(clusterPoint.x);

        int yStart = row;
        int yEnd = row;
        if (row > 0){
            yStart = row-1;
        }
        if (row < _numRows-1){
            yEnd = row+1;
        }


        int xStart = column;
        int xEnd = column;
        if (column > 0){
            xStart = column-1;
        }
        if (column < _numColumns-1){
            xEnd = column+1;
        }

        for (int i=xStart;i<=xEnd;i++){
            for (int j=yStart;j<=yEnd;j++){
                int index = this._numRows * j + i;
                ArrayList<Cluster> cell = _cells.get(index);
                if (cell == null) {
                    cell = new ArrayList<>();
                    _cells.put(index, cell);
                }
                cell.add(cluster);
            }
        }


    }

}

