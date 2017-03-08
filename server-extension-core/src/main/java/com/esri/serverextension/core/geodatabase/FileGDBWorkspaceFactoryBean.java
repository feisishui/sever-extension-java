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

package com.esri.serverextension.core.geodatabase;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartFactoryBean;

import com.esri.arcgis.datasourcesGDB.FileGDBWorkspaceFactory;
import com.esri.arcgis.geodatabase.IWorkspace;
import com.esri.arcgis.system.Cleaner;
import com.esri.arcgis.system.IPropertySet;
import com.esri.arcgis.system.PropertySet;

public class FileGDBWorkspaceFactoryBean implements
		SmartFactoryBean<IWorkspace>, DisposableBean {

	private String database;
	private IWorkspace workspace;

	public FileGDBWorkspaceFactoryBean() {
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	@Override
	public IWorkspace getObject() throws Exception {
		if (workspace == null) {
			IPropertySet propertySet = new PropertySet();
			if (database != null) {
				propertySet.setProperty("DATABASE", database);
			}
			FileGDBWorkspaceFactory fileGDBWorkspaceFactory = new FileGDBWorkspaceFactory();
			workspace = fileGDBWorkspaceFactory.open(propertySet, 0);

			// FileGDBWorkspaceFactory is a singleton.
			// It is a good practice to release the singletons
			// using com.esri.system.Cleaner.release()
			Cleaner.release(fileGDBWorkspaceFactory);
		}
		return workspace;
	}

	@Override
	public Class<?> getObjectType() {
		return IWorkspace.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public boolean isEagerInit() {
		return false;
	}

	@Override
	public boolean isPrototype() {
		return false;
	}

	@Override
	public void destroy() throws Exception {
		if (workspace != null) {
			Cleaner.release(workspace);
			workspace = null;
		}
	}

	@Override
	public String toString() {
		return "FileGDBWorkspaceFactoryBean [database=" + database + "]";
	}
}
