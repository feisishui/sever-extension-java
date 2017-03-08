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

import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.Cleaner;
import com.esri.serverextension.core.util.StopWatch;
import com.esri.serverextension.core.util.UncheckedIOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeodatabaseTemplate {
	
	private final Logger logger = LoggerFactory.getLogger(GeodatabaseTemplate.class);
			
	public GeodatabaseTemplate() {
	}

	public <T> List<T> query(IFeatureClass featureClass, IQueryFilter queryFilter, GeodatabaseObjectMapper<T> objectMapper) {
		return query(featureClass, queryFilter, new GeodatabaseObjectMapperCursorExtractor<T>(objectMapper));
	}

	public <T> T queryForObject(IFeatureClass featureClass, IQueryFilter queryFilter,
			GeodatabaseObjectMapper<T> objectMapper) {
		List<T> resultSet = query(featureClass, queryFilter, new GeodatabaseObjectMapperCursorExtractor<T>(
                objectMapper, 1));
		return resultSet.size() == 1 ? resultSet.get(0) : null;
	}

    public void query(IFeatureClass featureClass, IQueryFilter queryFilter,
                                GeodatabaseObjectCallbackHandler objectCallbackHandler) {
        query(featureClass, queryFilter, new GeodatabaseObjectCallbackHandlerCursorExtractor(
                objectCallbackHandler));
    }

	public <T> T query(IFeatureClass featureClass, IQueryFilter queryFilter,
                       GeodatabaseCursorExtractor<T> cursorExtractor) {
		IFeatureCursor cursor = null;
		T result = null;
		try {
			StopWatch stopWatch = StopWatch.createAndStart();
			cursor = featureClass.search(queryFilter, false);
			logger.debug("Preparing query took {} second(s).", stopWatch.stop()
					.elapsedTimeSeconds());
			stopWatch.start();
			GeodatabaseFieldMap fieldMap = new GeodatabaseFieldMap();
			fieldMap.initialize(cursor.getFields(), queryFilter != null ? queryFilter.getSubFields() : null);
			result = cursorExtractor.extractData(cursor, fieldMap);
			logger.debug(
					"Executing query and extracting data from cursor took {} second(s).",
					stopWatch.stop().elapsedTimeSeconds());
		} catch (IOException ex) {
			throw new UncheckedIOException("Failed to execute query.", ex);
		} finally {
			if (cursor != null) {
				Cleaner.release(cursor);
			}
		}
		return result;
	}
}
