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

package com.esri.serverextension.core.util;

import com.esri.arcgis.system.ServerUtilities;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LogAdaptorFactory implements ILoggerFactory {

    private final ConcurrentMap<String, Logger> loggerMap;
    private boolean showShortLogName;

    public LogAdaptorFactory() {
        loggerMap = new ConcurrentHashMap<String, Logger>();
    }

    public void setShowShortLogName(boolean showShortLogName) {
        this.showShortLogName = showShortLogName;
    }

    public boolean isShowShortLogName() {
        return showShortLogName;
    }

    /**
     * Return an appropriate {@link LogAdaptor} instance by name.
     */
    public Logger getLogger(String name) {
        if (name == null) {
            throw new NullPointerException("Argument 'name' is required.");
        }
        Logger logger = loggerMap.get(name);
        if (logger != null) {
            return logger;
        } else {
            Logger newInstance = new LogAdaptor(
                    ServerUtilities.getServerLogger(), name, showShortLogName);
            Logger oldInstance = loggerMap.putIfAbsent(name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }
}
