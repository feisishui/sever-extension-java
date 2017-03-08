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

import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.serverextension.core.geodatabase.FileGDBWorkspaceFactoryBean;
import com.esri.serverextension.core.geodatabase.GeodatabaseTemplate;
import com.esri.serverextension.core.rest.json.JSONGeometryMapper;
import com.esri.serverextension.core.util.ArcObjectsInitializer;
import com.esri.serverextension.core.util.ArcObjectsUtilities;
import com.esri.serverextension.core.util.StopWatch;
import com.esri.serverextension.test.AbstractArcObjectsIT;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@NotThreadSafe
@ContextConfiguration(locations = {"/spring/config/applicationContext-file-gdb-workspace-test.xml"})
public class ClusterAssemblerIT extends AbstractArcObjectsIT {

    @Inject
    private IWorkspace workspace;

    @Test
    public void testClusterAssembler(String where) throws IOException {
        System.out.println("1. Setting up query filter.");
        IFeatureClass featureClass = ((IFeatureWorkspace)workspace).openFeatureClass("Permit_Features");
        SpatialFilter spatialFilter = new SpatialFilter();
        spatialFilter.setSubFields("Valuation,Shape");
        spatialFilter.setGeometryField("Shape");
        spatialFilter.setSpatialRel(esriSpatialRelEnum.esriSpatialRelIntersects);
        JSONGeometryMapper geometryMapper = new JSONGeometryMapper();
        IEnvelope envelope = geometryMapper.readEnvelope("{\"xmin\":-13244092.36900171," +
                "\"ymin\":4000883.3498998554," +
                "\"xmax\":-13118812.079642477," +
                "\"ymax\":4061574.350358204," +
                "\"spatialReference\":{\"wkid\":102100}}");
        spatialFilter.setGeometryByRef(envelope);
        spatialFilter.setWhereClause(where);
        spatialFilter.setOutputSpatialReferenceByRef("Shape", ArcObjectsUtilities.createSpatialReference(102100));

        System.out.println("2. Executing query.");
        GeodatabaseTemplate geodatabaseTemplate = new GeodatabaseTemplate();
        ClusterAssemblerCallbackHandler clusterAssemblerCallbackHandler = new ClusterAssemblerCallbackHandler("Valuation");
        geodatabaseTemplate.query(featureClass, spatialFilter, clusterAssemblerCallbackHandler);
        System.out.println(String.format("# of input features: %1$d", clusterAssemblerCallbackHandler.getFeatureCount()));

        System.out.println("3. Building clusters.");
        ClusterExtent clusterExtent = new ClusterExtent(-13244092.36900171,
                4000883.3498998554,
                -13118812.079642477,
                4061574.350358204);
        ClusterAssembler clusterAssembler = new ClusterAssembler(
                clusterAssemblerCallbackHandler.getClusterFeatures(),
                76.43702828507277,
                100,
                clusterExtent);
        List<Cluster> clusters = clusterAssembler.getClusters();
        int clusterCount = 0;
        for (Cluster cluster : clusters) {
            System.out.println(String.format("Cluster %1$d: (x: %2$f y: %3$f), %4$f", ++clusterCount,
                    cluster.getPoint().x, cluster.getPoint().y, cluster.getValue()));
        }
    }

    public static void main(String[] args) throws Exception {
        StopWatch timer = StopWatch.createAndStart();
        ArcObjectsInitializer.getInstance().init();
        FileGDBWorkspaceFactoryBean fileGDBWorkspaceFactoryBean = new FileGDBWorkspaceFactoryBean();
        fileGDBWorkspaceFactoryBean.setDatabase("D:\\Development\\Projects\\sever-extension-java\\examples\\clustering-soe\\data\\Clustering\\Clustering.gdb");
        IWorkspace workspace = fileGDBWorkspaceFactoryBean.getObject();
        ClusterAssemblerIT clusterAssemblerIT = new ClusterAssemblerIT();
        clusterAssemblerIT.workspace = workspace;
        clusterAssemblerIT.testClusterAssembler("Issue_Date >= date '2017-01-01 00:00:00'");
        ArcObjectsInitializer.getInstance().shutdown();
        System.out.println(String.format("Time elapsed: %1$f", timer.stop().elapsedTimeSeconds()));
    }
}
