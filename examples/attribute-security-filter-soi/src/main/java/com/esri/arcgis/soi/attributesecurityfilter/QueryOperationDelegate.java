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
package com.esri.arcgis.soi.attributesecurityfilter;

import com.esri.arcgis.soe.template.rest.api.QueryMapServiceLayerOperationInput;
import com.esri.arcgis.soe.template.security.SecurityContext;
import com.esri.arcgis.soe.template.server.RestDelegate;
import com.esri.arcgis.soe.template.server.RestRequest;
import com.esri.arcgis.soe.template.server.RestResponse;
import com.esri.arcgis.soe.template.server.ServerObjectExtensionContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Set;

@Component
public class QueryOperationDelegate {

    protected final Logger logger = LoggerFactory.getLogger(QueryOperationDelegate.class);

    private ObjectMapper objectMapper;
    private AgencyIncidentDataAccessController accessController;

    public QueryOperationDelegate() {
    }

    @Inject
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Inject
    public void setAccessController(AgencyIncidentDataAccessController accessController) {
        this.accessController = accessController;
    }

    @RequestMapping("/layers/0/query")
    public RestResponse query(
            RestRequest request,
            RestDelegate handler,
            ServerObjectExtensionContext context,
            SecurityContext securityContext) throws IOException {
        logger.debug("Handling request for /layer/0/query ...");

        QueryMapServiceLayerOperationInput input = objectMapper.readValue(
                request.getOperationInput(), QueryMapServiceLayerOperationInput.class);

        Set<Integer> agencyIds = accessController.checkAccess(securityContext);
        StringBuilder agencyIdsString = new StringBuilder();
        for (Integer agencyId : agencyIds) {
            agencyIdsString.append(String.format("%1$d, ", agencyId));
        }
        if (agencyIdsString.length() > 0) {
            agencyIdsString.delete(agencyIdsString.length() - 2, agencyIdsString.length());
        }
        String agencyCondition = String.format("Responsible_Agency_ID IN (%1$s)", agencyIdsString);
        String where = input.getWhere();
        if (StringUtils.isEmpty(where)) {
            where = agencyCondition;
        } else {
            where = String.format("(%1$s) AND (%2$s)", where, agencyCondition);
        }
        input.setWhere(where);

        String filteredOperationInput = objectMapper.writeValueAsString(input);

        RestRequest filteredRequest = RestRequest.create(
                request.getCapabilities(),
                request.getResourceName(),
                request.getOperationName(),
                filteredOperationInput,
                request.getOutputFormat(),
                request.getRequestProperties(),
                request);

        return handler.process(filteredRequest, null);
    }
}
