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

package com.thoughtworks.go.helper;

import java.util.Arrays;
import java.util.List;

import com.thoughtworks.go.server.messaging.StubScheduleCheckCompletedListener;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ScheduleCheckMatcher {
    public static Matcher<String[]> scheduleCheckCompleted(final StubScheduleCheckCompletedListener listener) {
        return new TypeSafeMatcher<String[]>() {
            private List<String> expected;
            private List<String> actual;

            public boolean matchesSafely(String[] expected) {
                this.expected = Arrays.asList(expected);
                this.actual = listener.pipelines;
                return actual.containsAll(this.expected);
            }

            public void describeTo(Description description) {
                description.appendText(String.format("Expect to complete material checking for %s but got %s", expected, actual));
            }
        };
    }

}
