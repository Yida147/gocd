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

package com.thoughtworks.go.domain;

import java.io.File;
import java.util.HashMap;

import com.thoughtworks.go.config.AntTask;
import com.thoughtworks.go.config.FetchTask;
import com.thoughtworks.go.config.NantTask;
import com.thoughtworks.go.config.Tasks;
import com.thoughtworks.go.service.TaskFactory;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TasksTest {

    Pipeline pipeline = new NullPipeline();

    @Test
    public void shouldReturnEmptyTasks() throws Exception {
        AntTask antTask1 = new AntTask();
        FetchTask fetchArtifact = new FetchTask();
        Tasks tasks = new Tasks(antTask1, fetchArtifact);
        Tasks finds = tasks.findByType(NantTask.class);
        assertThat(finds.size(), is(0));
    }

    @Test
    public void shouldSetConfigAttributesForBuiltinTask() throws Exception {
        HashMap attributes = new HashMap();
        attributes.put(Tasks.TASK_OPTIONS, "ant");
        attributes.put("ant", antTaskAttribs("build.xml", "test", "foo"));
        TaskFactory taskFactory = mock(TaskFactory.class);
        AntTask antTask = new AntTask();
        when(taskFactory.taskInstanceFor(antTask.getTaskType())).thenReturn(antTask);
        Tasks tasks = new Tasks();
        Tasks spy = spy(tasks);
        spy.setConfigAttributes(attributes, taskFactory);

        assertThat(spy.size(), is(1));
        assertThat((AntTask) spy.get(0), is(antTask("build.xml", "test", "foo")));
    }

    @Test
    public void shouldIncrementIndexOfGivenTask() {
        Tasks tasks = new Tasks();
        AntTask task1 = antTask("b1", "t1", "w1");
        tasks.add(task1);
        AntTask task2 = antTask("b2", "t2", "w2");
        tasks.add(task2);
        AntTask task3 = antTask("b3", "t3", "w3");
        tasks.add(task3);

        tasks.incrementIndex(0);

        assertThat((AntTask) tasks.get(0), is(task2));
        assertThat((AntTask) tasks.get(1), is(task1));
        assertThat((AntTask) tasks.get(2), is(task3));
    }

    @Test
    public void shouldErrorOutWhenTaskIsNotFoundWhileIncrementing() {
        try {
            new Tasks().incrementIndex(1);
            fail("Should have thrown up");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("There is not valid task at position 1."));
        }
    }

    @Test
    public void shouldDecrementIndexOfGivenTask() {
        Tasks tasks = new Tasks();
        AntTask task1 = antTask("b1", "t1", "w1");
        tasks.add(task1);
        AntTask task2 = antTask("b2", "t2", "w2");
        tasks.add(task2);
        AntTask task3 = antTask("b3", "t3", "w3");
        tasks.add(task3);

        tasks.decrementIndex(2);

        assertThat((AntTask) tasks.get(0), is(task1));
        assertThat((AntTask) tasks.get(1), is(task3));
        assertThat((AntTask) tasks.get(2), is(task2));
    }

    @Test
    public void shouldErrorOutWhenTaskIsNotFoundWhileDecrementing() {
        try {
            new Tasks().decrementIndex(1);
            fail("Should have thrown up");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("There is not valid task at position 1."));
        }
    }

    private AntTask antTask(final String buildFile, final String target, final String workingDir) {
        AntTask antTask = new AntTask();
        antTask.setBuildFile(buildFile);
        antTask.setWorkingDirectory(workingDir);
        antTask.setTarget(target);
        return antTask;
    }

    private HashMap antTaskAttribs(final String buildFile, final String target, final String workingDir) {
        HashMap taskAttributes = new HashMap();
        taskAttributes.put(AntTask.BUILD_FILE, buildFile);
        taskAttributes.put(AntTask.WORKING_DIRECTORY, workingDir);
        taskAttributes.put(AntTask.TARGET, target);
        return taskAttributes;
    }

    public static Pipeline pipelineStub(final String label, final String defaultWorkingFolder) {
        return new NullPipeline() {
            @Override public String getLabel() {
                return label;
            }

            @Override public File defaultWorkingFolder() {
                return new File(defaultWorkingFolder);
            }
        };
    }
}
