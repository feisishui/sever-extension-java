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
package com.esri.arcgis.soe.template.geometry;

import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.ISpatialReferenceResolution;
import com.esri.arcgis.geometry.ISpatialReferenceTolerance;
import com.esri.arcgis.geometry.SpatialReferenceEnvironment;
import com.esri.arcgis.soe.template.util.ArcObjectsInteropException;
import com.esri.arcgis.system.Cleaner;

import java.io.IOException;

public class SpatialReferenceFactory {

	public SpatialReferenceFactory() {
	}

	public ISpatialReference create(int factoryCode) {
		try {
			SpatialReferenceEnvironment spatialReferenceEnvironment = new SpatialReferenceEnvironment();
			ISpatialReference sr = spatialReferenceEnvironment
					.createSpatialReference(factoryCode);
			ISpatialReferenceResolution resolution = (ISpatialReferenceResolution)sr;
			resolution.constructFromHorizon();
			ISpatialReferenceTolerance tolerance = (ISpatialReferenceTolerance)sr;
			tolerance.setDefaultXYTolerance();
			// SpatialReferenceEnvironment is a singleton.
			// It is a good practice to release the singletons
			// using com.esri.system.Cleaner.release()
			Cleaner.release(spatialReferenceEnvironment);
			return sr;
		} catch (IOException e) {
			throw new ArcObjectsInteropException(
					String.format(
							"Cannot create spatial reference from factory code '%1$d'.",
							factoryCode), e);
		}
	}
}
