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

package org.slf4j.impl;

import com.esri.serverextension.core.util.LogAdaptorFactory;
import org.slf4j.ILoggerFactory;

public class StaticLoggerBinder {

    public static String REQUESTED_API_VERSION = "1.7.24";

    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    private static final String LOGGER_FACTORY_CLASS_STR = LogAdaptorFactory.class
            .getName();

    private final ILoggerFactory loggerFactory;

    private StaticLoggerBinder() {
        loggerFactory = new LogAdaptorFactory();
    }

    public static final StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    public String getLoggerFactoryClassStr() {
        return LOGGER_FACTORY_CLASS_STR;
    }
}
