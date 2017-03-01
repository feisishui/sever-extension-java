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
package com.esri.serverextension.attributesecurityfilter;

import com.esri.arcgis.geodatabase.ICursor;
import com.esri.arcgis.geodatabase.IRow;
import com.esri.arcgis.geodatabase.ISqlWorkspace;
import com.esri.serverextension.core.security.ArcGISSecurityException;
import com.esri.serverextension.core.security.SecurityContext;
import com.esri.serverextension.core.util.ArcObjectsInteropException;
import com.esri.arcgis.system.Cleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class AgencyIncidentDataAccessController {

    private static final String AGENCY_ID_FOR_USER_NAME_SQL = "SELECT a.Agency_ID FROM Agency a " +
            "INNER JOIN Filter_Group_Agency ga ON (a.Agency_ID = ga.Agency_ID) " +
            "INNER JOIN Filter_User_Group ug ON (ga.Group_ID = ug.Group_ID) " +
            "INNER JOIN Filter_User u ON (ug.User_ID = u.User_ID) " +
            "WHERE u.User_Name = LOWER('%1$s')";

    private final Logger logger = LoggerFactory
            .getLogger(AgencyIncidentDataAccessController.class);

    private ISqlWorkspace sqlWorkspace;

    @Inject
    public AgencyIncidentDataAccessController(ISqlWorkspace sqlWorkspace) {
        this.sqlWorkspace = sqlWorkspace;
    }

    public Set<Integer> checkAccess(SecurityContext securityContext) {
        logger.debug("Performing agency incident data security check ...");
        Principal principal = securityContext.getUserPrincipal();
        String username = null;
        if (principal != null) {
            username = principal.getName();
        }
        if (StringUtils.isEmpty(username)) {
            logger.warn(
                    "Access to agency incident data forbidden for user: Anonymous");
            throw new ArcGISSecurityException(403, "Forbidden", null);
        }
        Set<Integer> agencyIds = getAgencyIds(username);
        if (agencyIds == null || agencyIds.size() == 0) {
            logger.warn("Access to agency incident data ID forbidden for user: {}",
                    username);
            throw new ArcGISSecurityException(403, "Forbidden", null);
        }
        logger.debug("Finished agency incident security check. "
                        + "User '{}' is permitted access to agency incident data for agencies [{}].",
                username, Arrays.toString(agencyIds.toArray(new Integer[agencyIds.size()])));
        return agencyIds;
    }

    private Set<Integer> getAgencyIds(String username) {
        String sql = String.format(AGENCY_ID_FOR_USER_NAME_SQL, username);
        logger.debug("Agency ID for username SQL: {}", sql);

        ICursor cursor = null;
        Set<Integer> agencyIds = new HashSet<>();
        try {
            cursor = (ICursor) sqlWorkspace.openQueryCursor(sql);
            IRow row = null;
            while ((row = cursor.nextRow()) != null) {
                agencyIds.add((Integer) row.getValue(0));
            }
            Cleaner.release(row);
        } catch (IOException ex) {
            throw new ArcObjectsInteropException(
                    String.format(
                            "Failed to query agency IDs for username '%1$s'. The query statement was: %2$s",
                            username, sql), ex);
        } finally {
            if (cursor != null) {
                Cleaner.release(cursor);
            }
        }
        return agencyIds;
    }
}
