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

package com.esri.serverextension.core.rest.api;

import com.esri.serverextension.core.server.DefaultConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * kyunam_kim@esri.com on 1/10/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DefaultConfig.class})
public class ParameterTest {

  @Autowired
  ObjectMapper objectMapper;

  @Before
  public void setUp() throws IOException {
//    EngineInitializer.initializeServer(esriLicenseServerEdition.esriLicenseServerEditionAdvanced);
//    AoInitialize aoInit = new AoInitialize();
//    aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeArcServer);
  }

  @Test
  public void testObjectMapper() {
    Assert.notNull(objectMapper);
  }

  @Test
  public void testMosaicRule0() throws IOException {
    String in = "{\"mosaicMethod\":\"esriMosaicNone\",\"ascending\":true,\"fids\":[1,2,3],\"mosaicOperation\":\"MT_FIRST\"}";
    MosaicRule rule = objectMapper.readValue(in, MosaicRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testMosaicRule1() throws IOException {
    String in = "{\"mosaicMethod\":\"esriMosaicCenter\",\"ascending\":true,\"mosaicOperation\":\"MT_FIRST\",\"where\":\"ImageType='Landsat7'\"}";
    MosaicRule rule = objectMapper.readValue(in, MosaicRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testMosaicRule2() throws IOException {
    String in = "{\"mosaicMethod\":\"esriMosaicViewpoint\",\"ascending\":true,\"viewpoint\":{\"x\":100,\"y\":90,\"spatialReference\":{\"wkid\":4326}},\"mosaicOperation\":\"MT_FIRST\"}";
    MosaicRule rule = objectMapper.readValue(in, MosaicRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testMosaicRule3() throws IOException {
    String in = "{\"mosaicMethod\":\"esriMosaicAttribute\",\"sortField\":\"acquisitionDate\",\"sortValue\":\"2010/12/31\",\"ascending\":true,\"mosaicOperation\":\"MT_FIRST\"}";
    MosaicRule rule = objectMapper.readValue(in, MosaicRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testMosaicRule4() throws IOException {
    String in = "{\"mosaicMethod\":\"esriMosaicLockRaster\",\"lockRasterIds\":[1,2,3],\"ascending\":true,\"mosaicOperation\":\"MT_FIRST\"}";
    MosaicRule rule = objectMapper.readValue(in, MosaicRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testMosaicRule5() throws IOException {
    String in = "{\"mosaicMethod\":\"esriMosaicLockRaster\",\"lockRasterIds\":[1],\"ascending\":true,\"mosaicOperation\":\"MT_FIRST\",\"itemRenderingRule\":{\"rasterFunction\":\"Recast\",\"rasterFunctionArguments\":{\"ClassTypes\":[5,6]}}}";
    MosaicRule rule = objectMapper.readValue(in, MosaicRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testMosaicRule6() throws IOException {
    String in = "{\"mosaicMethod\":\"esriMosaicAttribute\",\"sortField\":\"acquisitionDate\",\"sortValue\":\"2010/12/31\",\"ascending\":true,\"mosaicOperation\":\"MT_FIRST\",\"itemRenderingRule\":{\"rasterFunction\":\"Colormap\",\"rasterFunctionArguments\":{\"Colormap\":[[0,255,0,0],[1,0,255,0],[2,0,0,255],[3,255,255,0]],\"Raster\":{\"rasterFunction\":\"TransposeBits\",\"rasterFunctionArguments\":{\"InputBitPositions\":[12,13],\"OutputBitPositions\":[0,1],\"ConstantFillValue\":0}}}}}";
    MosaicRule rule = objectMapper.readValue(in, MosaicRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testRenderingRule0() throws IOException {
    String in = "{\"rasterFunction\":\"Hillshade\",\"rasterFunctionArguments\":{\"Azimuth\":215,\"Altitude\":75,\"ZFactor\":0.3},\"variableName\":\"DEM\"}";
    RenderingRule rule = objectMapper.readValue(in, RenderingRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testRenderingRule1() throws IOException {
    String in = "{\"rasterFunction\":\"ShadedRelief\",\"rasterFunctionArguments\":{\"Azimuth\":215,\"Altitude\":75,\"ZFactor\":0.3,\"Colormap\":[[1,255,0,0],[2,0,255,0],[3,125,25,255]]},\"variableName\":\"Raster\"}";
    RenderingRule rule = objectMapper.readValue(in, RenderingRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testRenderingRule2() throws IOException {
    String in = "{\"rasterFunction\":\"NDVI\",\"rasterFunctionArguments\":{\"VisibleBandID\":2,\"InfraredBandID\":1},\"variableName\":\"Raster\"}";
    RenderingRule rule = objectMapper.readValue(in, RenderingRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testRenderingRule3() throws IOException {
    String in = "{\"rasterFunction\":\"Stretch\",\"rasterFunctionArguments\":{\"StretchType\":5,\"Statistics\":[[50,200,56.7,54.8],[50,200,97.5,94.5],[50,200,87.5,87.3]],\"Min\":100,\"Max\":255,\"UseGamma\":false},\"outputPixelType\":\"U8\",\"variableName\":\"Raster\"}";
    RenderingRule rule = objectMapper.readValue(in, RenderingRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testRenderingRule4() throws IOException {
    String in = "{\"rasterFunction\":\"Remap\",\"rasterFunctionArguments\":{\"InputRanges\":[50,51],\"OutputValues\":[71],\"GeometryType\":\"esriGeometryEnvelope\",\"Geometries\":[{\"spatialReference\":{\"wkid\":2264},\"rings\":[[[1432285,529578],[1436048,529578],[1434048,531578],[1433285,533205],[1432285,529578]]]}]}}";
    RenderingRule rule = objectMapper.readValue(in, RenderingRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testRenderingRule5() throws IOException {
    String in = "{\"rasterFunction\":\"Clip\",\"rasterFunctionArguments\":{\"ClippingGeometry\":{\"rings\":[[[10,10],[9,11],[10,12],[11,11],[12,10],[10,10]]],\"spatialReference\":{\"wkid\":4326}},\"extent\":{\"xmin\":10,\"ymin\":10,\"xmax\":12,\"ymax\":12,\"spatialReference\":{\"wkid\":4326}},\"ClippingType\":1},\"variableName\":\"Raster\"}";
    RenderingRule rule = objectMapper.readValue(in, RenderingRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testRenderingRule6() throws IOException {
    String in = "{\"rasterFunction\":\"Geometric\",\"rasterFunctionArguments\":{\"GeodataTransforms\":[{\"geodataTransform\":\"Polynomial\",\"geodataTransformArguments\":{\"sourcePoints\":[{\"x\":0,\"y\":0},{\"x\":0,\"y\":200},{\"x\":200,\"y\":0}],\"targetPoints\":[{\"x\":100,\"y\":100},{\"x\":100,\"y\":300},{\"x\":300,\"y\":100}],\"polynomialOrder\":1,\"spatialReference\":{\"wkid\":54004}}}],\"AppendGeodataXform\":true},\"variableName\":\"Raster\"}";
    RenderingRule rule = objectMapper.readValue(in, RenderingRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testRenderingRule7() throws IOException {
    String in = "{\"rasterFunction\":\"Local\",\"rasterFunctionArguments\":{\"Operation\":2,\"Rasters\":[\"$$\",100]}}";
    RenderingRule rule = objectMapper.readValue(in, RenderingRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testRenderingRule8() throws IOException {
    String in = "{\"rasterFunction\":\"Resample\",\"rasterFunctionArguments\":{\"ResamplingType\":10,\"InputCellsize\":{\"x\":0.125,\"y\":0.125}}}";
    RenderingRule rule = objectMapper.readValue(in, RenderingRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }

  @Test
  public void testRenderingRule9() throws IOException {
    String in = "{\"rasterFunction\":\"Classify\",\"rasterFunctionArguments\":{\"ClassifierDefinition\":{\"EsriClassifierDefinitionFile\":0,\"FileVersion\":1,\"NumberDefinitions\":1,\"Definitions\":[{\"Classifier\":\"MaximumLikelihood\",\"NumberClasses\":2,\"RasterIsSegmented\":false,\"NumberBands\":3,\"Classes\":[{\"ClassValue\":1,\"ClassName\":\"Vegetation\",\"Red\":230,\"Green\":0,\"Blue\":0,\"Count\":546,\"Means\":[244.9359,14.88278,15.21978],\"Covariances\":[[10.26146,0.1390063,0.1074951],[0.1390063,10.31593,0.0001207584],[0.1074951,0.0001207584,10.19345]]},{\"ClassValue\":2,\"ClassName\":\"Water\",\"Red\":0,\"Green\":77,\"Blue\":168,\"Count\":716,\"Means\":[15.05587,15.01257,244.9567],\"Covariances\":[[10.18124,0.1152196,0.2789551],[0.1152196,10.02638,-0.3583943],[0.2789551,-0.3583943,10.11684]]}]}]}}}";
    RenderingRule rule = objectMapper.readValue(in, RenderingRule.class);
    Assert.notNull(rule);
    String out = objectMapper.writeValueAsString(rule);
    System.out.println(in);
    System.out.println(out);
  }
}
