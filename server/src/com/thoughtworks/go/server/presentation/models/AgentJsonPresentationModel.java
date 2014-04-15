/*************************GO-LICENSE-START*********************************
 * Copyright 2014 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************GO-LICENSE-END***********************************/

package com.thoughtworks.go.server.presentation.models;

import com.thoughtworks.go.domain.AgentInstance;
import com.thoughtworks.go.domain.AgentStatus;
import com.thoughtworks.go.util.json.JsonMap;
import com.thoughtworks.go.util.FileUtil;
import com.thoughtworks.go.util.StringUtil;
import com.thoughtworks.go.util.TimeConverter;
import org.apache.commons.lang.StringUtils;

public class AgentJsonPresentationModel {
    private final AgentInstance agentInstance;
    private final TimeConverter timeConverter = new TimeConverter();
    private static final String UNKNOWN = "unknown";


    public AgentJsonPresentationModel(AgentInstance agentInstance) {
        this.agentInstance = agentInstance;
    }

    public JsonMap toJsonHash() {
        JsonMap map = new JsonMap();
        map.put("location", agentInstallPath());
        map.put("agentId", agentInstance.agentConfig().getUuid());
        map.put("hostname", agentInstance.agentConfig().getHostNameForDispaly());
        map.put("status", agentInstance.getStatus().toString().toLowerCase());
        map.put("usablespace", getUsableSpaceForDisplay());
        map.put("isLowSpace", String.valueOf(agentInstance.isLowDiskSpace()));
        if (agentInstance.canApprove()) {
            map.put("canApprove", "true");
        }
        if (agentInstance.canDisable()) {
            map.put("canDeny", "true");
        }
        if (agentInstance.getStatus() != AgentStatus.Pending) {
            map.put("canEditResource", "true");
        }
        map.put("humanizedStatus", StringUtil.humanize(agentInstance.getStatus().toString()));
        if (agentInstance.getBuildingInfo().isBuilding()) {
            map.put("buildingInfo", agentInstance.getBuildingInfo().getBuildingInfo());
            map.put("buildLocator", agentInstance.getBuildingInfo().getBuildLocator());
        }
        if (agentInstance.getStatus() == AgentStatus.LostContact) {
            map.put("lostContactTime", timeConverter.nullSafeDate(agentInstance.getLastHeardTime()));
        }
        map.put("ipAddress", agentInstance.agentConfig().getIpAddress());
        map.put("resources", agentInstance.agentConfig().getResources().resourceNames());
        map.put("isCancelled", String.valueOf(agentInstance.isCancelled()));
        return map;
    }

    private String agentInstallPath() {
        String location = agentInstance.getLocation();
        return StringUtils.isEmpty(location) ? UNKNOWN : location;
    }

    private String getUsableSpaceForDisplay() {
        if (agentInstance.getUsableSpace() == null) {
            return UNKNOWN;
        } else {
            return FileUtil.byteCountToDisplaySize(agentInstance.getUsableSpace()) + " free";
        }
    }


}
