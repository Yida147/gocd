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

package com.thoughtworks.go.domain.materials;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import com.thoughtworks.go.config.CaseInsensitiveString;
import com.thoughtworks.go.config.PipelineConfig;
import com.thoughtworks.go.config.materials.SubprocessExecutionContext;
import com.thoughtworks.go.domain.MaterialInstance;
import com.thoughtworks.go.domain.MaterialRevision;
import com.thoughtworks.go.util.command.EnvironmentVariableContext;
import com.thoughtworks.go.util.command.ProcessOutputStreamConsumer;
import com.thoughtworks.go.util.json.JsonMap;

public interface Material extends Serializable {

    //-- DB and config behaviour

    String getFolder();

    CaseInsensitiveString getName();

    //-- SCM behaviour
    //better:
    //scm = material.createScm(workingDirectory, output)
    //scm.updateTo(revision)
    //scm.findModificationsSince(revision)
    //scm.findRecentModifications(5)

    void updateTo(ProcessOutputStreamConsumer outputStreamConsumer, Revision revision, File baseDir, final SubprocessExecutionContext execCtx);

    void toJson(JsonMap jsonMap, Revision revision);

    boolean matches(String name, String regex);

    void emailContent(StringBuilder content, Modification modification);

    void setId(long id);

    Map<String, Object> getSqlCriteria();

    MaterialInstance createMaterialInstance();

    String getDescription();

    String getFingerprint();

    String getPipelineUniqueFingerprint();

    String getTypeForDisplay();

    void populateEnvironmentContext(EnvironmentVariableContext context, MaterialRevision materialRevision, File workingDir);

    String getDisplayName();

    String getType();

    String getTruncatedDisplayName();

    String getShortRevision(String revision);

    boolean isAutoUpdate();

    MatchedRevision createMatchedRevision(Modification modifications, String searchString);

    String getUriForDisplay();

    boolean isSameFlyweight(Material other);

    boolean hasSameFingerprint(MaterialConfig materialConfig);

    long getId();

    Map<String, Object> getAttributesForXml();

    Boolean isUsedInFetchArtifact(PipelineConfig pipelineConfig);

    Class getInstanceType();

    Revision oldestRevision(Modifications modifications);

    String getLongDescription();

    MaterialConfig config();
}
