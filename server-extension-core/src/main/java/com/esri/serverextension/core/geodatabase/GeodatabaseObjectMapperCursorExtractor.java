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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeodatabaseObjectMapperCursorExtractor<T> implements
        GeodatabaseCursorExtractor<List<T>> {

	private final Logger logger = LoggerFactory
			.getLogger(GeodatabaseObjectMapperCursorExtractor.class);

	private final GeodatabaseObjectMapper<T> objectMapper;
	private final int objectsExpected;

	public GeodatabaseObjectMapperCursorExtractor(GeodatabaseObjectMapper<T> objectMapper) {
		this(objectMapper, -1);
	}

	public GeodatabaseObjectMapperCursorExtractor(GeodatabaseObjectMapper<T> objectMapper, int objectsExpected) {
		this.objectMapper = objectMapper;
		this.objectsExpected = objectsExpected;
	}

	@Override
	public List<T> extractData(ICursor cursor, IField[] fields)
			throws IOException {
		try {
			StopWatch stopWatch = StopWatch.createAndStart();

			objectMapper.setFields(fields);

			List<T> resultSet = objectsExpected > 0 ? new ArrayList<T>(
                    objectsExpected) : new ArrayList<T>();
			IRow row = null;
			int rowCount = 0;
			if ((row = cursor.nextRow()) != null) {
				logger.debug("Executing query took {} second(s).", stopWatch
						.stop().elapsedTimeSeconds());
				stopWatch.start();
				rowCount++;
				resultSet.add(objectMapper.mapRow(row));
                Cleaner.release(row);
			} else {
				logger.debug("Query returned no rows.");
				return resultSet;
			}
			if (objectsExpected == 1) {
				if ((row = cursor.nextRow()) != null) {
					Cleaner.release(row);
					throw new IncorrectResultSizeDataAccessException(
							"Expected just one row but found more.", 1);
				}
			} else {
				while ((row = cursor.nextRow()) != null) {
					rowCount++;
					resultSet.add(objectMapper.mapRow(row));
                    Cleaner.release(row);
				}
			}
			logger.debug("Extracting {} row(s) took {} second(s).", rowCount,
					stopWatch.stop().elapsedTimeSeconds());
			return resultSet;
		} catch (AutomationException ex) {
			throw new GeodatabaseSystemException("Failed to extract data from cursor.",
					ex);
		} finally {
			if (cursor != null) {
				Cleaner.release(cursor);
			}
		}
	}

    @Override
    public List<T> extractData(IFeatureCursor featureCursor, IField[] fields) throws IOException {
        try {
            StopWatch stopWatch = StopWatch.createAndStart();

            objectMapper.setFields(fields);

            List<T> resultSet = objectsExpected > 0 ? new ArrayList<T>(
                    objectsExpected) : new ArrayList<T>();
            IFeature feature = null;
            int featureCount = 0;
            if ((feature = featureCursor.nextFeature()) != null) {
                logger.debug("Executing query took {} second(s).", stopWatch
                        .stop().elapsedTimeSeconds());
                stopWatch.start();
                featureCount++;
                resultSet.add(objectMapper.mapRow(feature));
                Cleaner.release(feature);
            } else {
                logger.debug("Query returned no features.");
                return resultSet;
            }
            if (objectsExpected == 1) {
                if ((feature = featureCursor.nextFeature()) != null) {
                    Cleaner.release(feature);
                    throw new IncorrectResultSizeDataAccessException(
                            "Expected just one feature but found more.", 1);
                }
            } else {
                while ((feature = featureCursor.nextFeature()) != null) {
                    featureCount++;
                    resultSet.add(objectMapper.mapRow(feature));
                    Cleaner.release(feature);
                }
            }
            logger.debug("Extracting {} features(s) took {} second(s).", featureCount,
                    stopWatch.stop().elapsedTimeSeconds());
            return resultSet;
        } catch (AutomationException ex) {
            throw new GeodatabaseSystemException("Failed to extract data from cursor.",
                    ex);
        } finally {
            if (featureCursor != null) {
                Cleaner.release(featureCursor);
            }
        }
    }
}
