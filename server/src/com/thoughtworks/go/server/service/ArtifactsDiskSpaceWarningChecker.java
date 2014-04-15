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

package com.thoughtworks.go.server.service;

import com.thoughtworks.go.server.messaging.EmailMessageDrafter;
import com.thoughtworks.go.server.messaging.SendEmailMessage;
import com.thoughtworks.go.server.service.result.OperationResult;
import com.thoughtworks.go.serverhealth.HealthStateLevel;
import com.thoughtworks.go.serverhealth.ServerHealthService;
import com.thoughtworks.go.util.SystemEnvironment;
import org.apache.log4j.Logger;

import static com.thoughtworks.go.server.service.ArtifactsDiskSpaceFullChecker.ARTIFACTS_DISK_FULL_ID;

public class ArtifactsDiskSpaceWarningChecker extends DiskSpaceChecker {
    private static final Logger LOGGER = Logger.getLogger(ArtifactsDiskSpaceWarningChecker.class);
    private final ServerHealthService serverHealthService;

    public ArtifactsDiskSpaceWarningChecker(SystemEnvironment systemEnvironment, EmailSender warningEmailSender, GoConfigService goConfigService,
                                            final SystemDiskSpaceChecker diskSpaceChecker, final ServerHealthService serverHealthService) {
        super(warningEmailSender, systemEnvironment, goConfigService.artifactsDir(), goConfigService, ARTIFACTS_DISK_FULL_ID, diskSpaceChecker);
        this.serverHealthService = serverHealthService;
    }

    protected long limitInMb() {
        return systemEnvironment.getArtifactReposiotryWarningLimit();
    }

    protected void createFailure(OperationResult result, long size, long availableSpace) {
        String msg = "Go has less than " + size + "M of disk space available to it."; LOGGER.warn(msg);
        LOGGER.warn(msg);
        result.warning("Go Server's artifact repository is running low on disk space", msg, ARTIFACTS_DISK_FULL_ID);
    }

    protected SendEmailMessage createEmail() {
        return EmailMessageDrafter.lowArtifactsDiskSpaceMessage(systemEnvironment, getAdminMail(), targetFolderCanonicalPath(), constructHelpUrl());
    }

    @Override public void check(OperationResult result) {
        if (! serverHealthService.containsError(ARTIFACTS_DISK_FULL_ID, HealthStateLevel.ERROR)) {
            super.check(result);
        }
    }
}
