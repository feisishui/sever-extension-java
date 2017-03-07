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

package com.esri.serverextension.cluster;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.esri.arcgis.server.json.JSONObject;

@Service
public class AboutResource {

	private String projectName;
	private String projectDescription;
	private String projectVersion;
	private String gitBranch;
	private String gitCommitID;

	public AboutResource() {
	}

	@Resource(name = "projectName")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Resource(name = "projectDescription")
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	@Resource(name = "projectVersion")
	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}

	@Resource(name = "gitBranch")
	public void setGitBranch(String gitBranch) {
		this.gitBranch = gitBranch;
	}

	@Resource(name = "gitCommitID")
	public void setGitCommitID(String gitCommitID) {
		this.gitCommitID = gitCommitID;
	}

	@RequestMapping("/about")
	public JSONObject getAboutResource() {
		JSONObject aboutResource = new JSONObject();
		aboutResource.put("name", projectName);
		aboutResource.put("description", projectDescription);
		aboutResource.put("version", projectVersion);
		aboutResource.put("gitBranch", gitBranch);
		aboutResource.put("gitCommitID", gitCommitID);
		return aboutResource;
	}

}
