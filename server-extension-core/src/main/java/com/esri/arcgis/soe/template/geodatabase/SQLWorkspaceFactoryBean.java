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
package com.esri.arcgis.soe.template.geodatabase;

import com.esri.arcgis.datasourcesGDB.SdeWorkspaceFactory;
import com.esri.arcgis.datasourcesGDB.SqlWorkspace;
import com.esri.arcgis.geodatabase.ISqlWorkspace;
import com.esri.arcgis.geodatabase.IWorkspace;
import com.esri.arcgis.system.Cleaner;
import com.esri.arcgis.system.IPropertySet;
import com.esri.arcgis.system.PropertySet;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartFactoryBean;

/**
 * This class is intended for use in integration tests that require an enterprise geodatabase connection.
 */
public class SQLWorkspaceFactoryBean implements
        SmartFactoryBean<ISqlWorkspace>, DisposableBean {

    private String server;
    private String instance;
    private String dbClient;
    private String dbConnectionProperties;
    private String database;
    private String authenticationMode;
    private String user;
    private String password;
    private ISqlWorkspace sqlWorkspace;

    public SQLWorkspaceFactoryBean() {
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getDbClient() {
        return dbClient;
    }

    public void setDbClient(String dbClient) {
        this.dbClient = dbClient;
    }

    public String getDbConnectionProperties() {
        return dbConnectionProperties;
    }

    public void setDbConnectionProperties(String dbConnectionProperties) {
        this.dbConnectionProperties = dbConnectionProperties;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getAuthenticationMode() {
        return authenticationMode;
    }

    public void setAuthenticationMode(String authenticationMode) {
        this.authenticationMode = authenticationMode;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public ISqlWorkspace getObject() throws Exception {
        if (sqlWorkspace == null) {
            IPropertySet propertySet = new PropertySet();
            if (server != null) {
                propertySet.setProperty("SERVER", server);
            }
            if (instance != null) {
                propertySet.setProperty("INSTANCE", instance);
            }
            if (dbClient != null) {
                propertySet.setProperty("DBCLIENT", dbClient);
            }
            if (dbConnectionProperties != null) {
                propertySet.setProperty("DB_CONNECTION_PROPERTIES",
                        dbConnectionProperties);
            }
            if (database != null) {
                propertySet.setProperty("DATABASE", database);
            }
            if (authenticationMode != null) {
                propertySet.setProperty("AUTHENTICATION_MODE",
                        authenticationMode);
            }
            if (user != null) {
                propertySet.setProperty("USER", user);
            }
            if (password != null) {
                propertySet.setProperty("PASSWORD", password);
            }
            propertySet.setProperty("IS_GEODATABASE", Boolean.FALSE);
            SdeWorkspaceFactory sdeWorkspaceFactory = new SdeWorkspaceFactory();
            IWorkspace sdeWorkspace = sdeWorkspaceFactory.open(propertySet, 0);

            sqlWorkspace = new SqlWorkspace(sdeWorkspace);

            // SdeWorkspaceFactory is a singleton.
            // It is a good practice to release the singletons
            // using com.esri.system.Cleaner.release()
            Cleaner.release(sdeWorkspaceFactory);
        }
        return sqlWorkspace;
    }

    @Override
    public Class<?> getObjectType() {
        return ISqlWorkspace.class;
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
        if (sqlWorkspace != null) {
            Cleaner.release(sqlWorkspace);
            sqlWorkspace = null;
        }
    }

    @Override
    public String toString() {
        return "SQLWorkspaceFactoryBean [server=" + server + ", instance="
                + instance + ", dbClient=" + dbClient
                + ", dbConnectionProperties=" + dbConnectionProperties
                + ", database=" + database + ", authenticationMode="
                + authenticationMode + ", user=" + user + ", password="
                + "*****" + "]";
    }
}
