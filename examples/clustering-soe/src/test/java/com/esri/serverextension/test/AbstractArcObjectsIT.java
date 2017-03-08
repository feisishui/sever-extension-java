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

package com.esri.serverextension.test;

import com.esri.arcgis.interop.AutomationException;
import com.esri.serverextension.core.util.ArcObjectsInitializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.net.UnknownHostException;

public abstract class AbstractArcObjectsIT {

	public AbstractArcObjectsIT() {
	}

	@BeforeClass
	public static void init() throws UnknownHostException, IOException {
		ArcObjectsInitializer.getInstance().init();
	}

	@AfterClass
	public static void shutdown() throws AutomationException, IOException {
		ArcObjectsInitializer.getInstance().shutdown();
	}
}
