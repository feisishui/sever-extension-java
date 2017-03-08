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

import com.esri.serverextension.core.rest.api.Extent;
import com.esri.serverextension.core.rest.api.Feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Assembles the cluster.
 *
 * Created by kcoffin on 2/8/17.
 *
 */
public class ClusterAssembler {

    //The grid's extent
    private Extent bbox;

    //cellsize of grid
    private double cellSize;

    private double mapUnitsPerPixel;

    //number of columns in grid
    private int numColumns;

    //number of columns in grid
    private int numRows;

    //the cells which keeps the cluster info
    private Map<Integer, ArrayList<Cluster>> cells;

    //the desired cluster distance in pixels
    private double clusterDistanceInPixels;

    //All the clusters which are created
    private List<Cluster> clusters;

    // The field to determine a clusters weight
    private String clusterFieldName;

    /**
     * Creates a new cluster assembler.
     *
     * @param mapUnitsPerPixel        map units per pixel (like meters per pixel)
     * @param clusterDistanceInPixels cluster distance in pixels
     * @param bbox                    the extent in real world coordinates
     * @param clusterFieldName        the field to determine a clusters weight
     */
    public ClusterAssembler(double mapUnitsPerPixel,
                            double clusterDistanceInPixels,
                            Extent bbox,
                            String clusterFieldName) {
        cells = new HashMap<>();
        this.mapUnitsPerPixel = mapUnitsPerPixel;
        this.clusterDistanceInPixels = clusterDistanceInPixels;
        cellSize = mapUnitsPerPixel * this.clusterDistanceInPixels;
        this.bbox = bbox;
        numColumns = getGridColumn(bbox.getWidth()) + 1;
        numRows = getGridRow(bbox.getHeight()) + 1;
        clusters = new ArrayList<>();
        this.clusterFieldName = clusterFieldName;
    }

    /**
     * Returns the assembled clusters.
     *
     * @return the assembled clusters
     */
    public List<Cluster> getClusters() {
        return clusters;
    }

    /**
     * Builds the clusters. Call this method after all features have been added and before accessing the
     * assembled clusters.
     */
    public void buildClusters() {
//        for (Cluster cluster : clusters) {
//            fixCluster(cluster);
//        }
//        for (Cluster cluster : clusters) {
//            fixCluster(cluster);
//        }
    }

    /**
     * Adds a feature.
     *
     * @param feature
     */
    public void addFeature(Feature feature) {
        Cluster closestCluster = getClosestCluster(new ClusterPoint(feature.getGeometry()));

        if (closestCluster != null) {
            addFeatureToCluster(feature, closestCluster);
        } else {
            createCluster(feature);//create new cluster
        }

    }

    //from yValue real-world, what is the grid row
    private int getGridRow(double yValue) {
        return (int) Math.floor((yValue - bbox.getYmin()) / cellSize);
    }

    //from xValue real-world, what is the grid column
    private int getGridColumn(double xValue) {
        return (int) Math.floor((xValue - bbox.getXmin()) / cellSize);
    }

    //add a feature to an EXISTING cluster
    private void addFeatureToCluster(Feature feature, Cluster cluster) {
        //remove it from the grid because its coordinates are going to change
        removeClusterFromGrid(cluster);

        //add the feature to the cluster
        cluster.addFeature(feature, getFeatureValue(feature));

        //add it back in to the grid
        addClusterToGrid(cluster);
    }

    //remove a cluster from the grid (it is already in the grid)
    private void removeClusterFromGrid(Cluster cluster) {
        ClusterPoint pt = cluster.getPoint();
        int row = getGridRow(pt.y);
        int column = getGridColumn(pt.x);
        int index = numRows * row + column;
        ArrayList<Cluster> cell = cells.get(index);

        if (cell != null) {
            cell.remove(cluster);
        } else {
            System.out.println("Programming error");
        }
    }

    //Add the cluster to the grid
    private void addClusterToGrid(Cluster cluster) {
        ClusterPoint pt = cluster.getPoint();
        int row = getGridRow(pt.y);
        int column = getGridColumn(pt.x);
        int index = numRows * row + column;
        ArrayList<Cluster> cell = cells.get(index);

        if (cell == null) {
            cell = new ArrayList<>();
            cells.put(index, cell);
        }
        cell.add(cluster);
    }

    private void createCluster(Feature feature) {
        Cluster cluster = new Cluster(feature, getFeatureValue(feature));
        addClusterToGrid(cluster);
        clusters.add(cluster);
    }


    /**
     * Gets the closest cluster within the cell distance
     *
     * @param pt
     * @return
     */
    private Cluster getClosestCluster(ClusterPoint pt) {
        int row = getGridRow(pt.y);
        int column = getGridColumn(pt.x);

        //should never happen as all features should come from within the extent
        if (row < 0 || column < 0 || row >= numRows || column >= numColumns) {
            System.out.println("There's an error in the query");
            return null;
        }

        int yStart = row;
        int yEnd = row;
        if (row > 0) {
            yStart = row - 1;
        }
        if (row < numRows - 1) {
            yEnd = row + 1;
        }


        int xStart = column;
        int xEnd = column;
        if (column > 0) {
            xStart = column - 1;
        }
        if (column < numColumns - 1) {
            xEnd = column + 1;
        }

        /*
        int xStart = (int)(Math.floor((extent.xmin - this._xmin) / this.cellSize));
        int xEnd = (int)(Math.floor((extent.xmax - this._xmin) / this.cellSize));
        int yStart = (int)(Math.floor((extent.ymin - this._ymin) / this.cellSize));
        int yEnd = (int)(Math.floor((extent.ymax - this._ymin) / this.cellSize));
        */

        Cluster minCluster = null;
        double minDis2 = Double.MAX_VALUE;

        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                int index = numRows * y + x;
                ArrayList<Cluster> cell = cells.get(index);
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
        if (minDis2 > 0) {
            minDis2 = Math.sqrt(minDis2);
        }
        if (minDis2 > cellSize) {
            return null;
        }
        return minCluster;
    }


    //This examines a cluster and determines if all features in it are the closest to it.
    private void examineCluster(Cluster cluster) {
        List<Feature> features = cluster.getFeatures();
        for (int k = 0; k < features.size(); k++) {
            Feature feature = features.get(k);
            ClusterPoint p = new ClusterPoint(feature.getGeometry());
            Cluster clust = getClosestCluster(p);
            if (clust != cluster) {
                double pixels = cluster.getPoint().distance(p) / mapUnitsPerPixel;
                System.out.print("Not closest to cluster...pixel Distance=" + Math.round(pixels));
                if (clust == null) {
                    System.out.println();
                } else {
                    double closerPixels = clust.getPoint().distance(p) / mapUnitsPerPixel;
                    System.out.println("   Closer to distance=" + Math.round(closerPixels));
                }
            }
        }
    }


    private void fixCluster(Cluster cluster) {
        double distance = cellSize;//this.mapUnitsPerPixel*this.clusterDistanceInPixels;
        double distanceSq = distance * distance;
        double test = distanceSq * 2.0;
        List<Feature> features = cluster.getFeatures();
        for (int k = features.size() - 1; k >= 0; k--) {
            Feature feature = features.get(k);
            ClusterPoint p = new ClusterPoint(feature.getGeometry());

            Cluster minPt = this.getClosestCluster(p);

            if (minPt != null && minPt != cluster) {
                double featureValue = getFeatureValue(feature);

                double clusterValue = cluster.getValue();

                //features.splice(k, 1);
                features.remove(k);

                double newClusterValue = clusterValue - featureValue;
                cluster.setValue(newClusterValue);
                ClusterPoint clusterPoint = cluster.getPoint();
                if (newClusterValue > 0) {
                    double ptc = featureValue / newClusterValue;
                    double ctc = clusterValue / newClusterValue;

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
                minPt.addPointCluster(feature, featureValue);
            }
        }
    }

    private void addCellsForAllClusters() {
        //this.cells = [];
        cells = new HashMap<>();
        for (Cluster cluster : clusters) {
            addCells(cluster);
        }

    }

    private void addCells(Cluster cluster) {
        ClusterPoint clusterPoint = cluster.getPoint();
        int row = getGridRow(clusterPoint.y);
        int column = getGridColumn(clusterPoint.x);

        int yStart = row;
        int yEnd = row;
        if (row > 0) {
            yStart = row - 1;
        }
        if (row < numRows - 1) {
            yEnd = row + 1;
        }


        int xStart = column;
        int xEnd = column;
        if (column > 0) {
            xStart = column - 1;
        }
        if (column < numColumns - 1) {
            xEnd = column + 1;
        }

        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                int index = this.numRows * j + i;
                ArrayList<Cluster> cell = cells.get(index);
                if (cell == null) {
                    cell = new ArrayList<>();
                    cells.put(index, cell);
                }
                cell.add(cluster);
            }
        }


    }

    private double getFeatureValue(Feature feature) {
        Object value = feature.getAttributes().get(clusterFieldName);
        if (value == null) {
            return 0.0d;
        }
        if (value instanceof Number) {
            return ((Number)feature.getAttributes().get(clusterFieldName)).doubleValue();
        }
        throw new IllegalArgumentException("Cluster field type must be numeric.");
    }

}

