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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class BuildOutputMatcher implements OutputMatcher {
    private static final String TARGET_REGEX = "^-*([\\w\\.\\-\\?]+):*$";
    private static final Pattern COMPILED_PATTERN = Pattern.compile(TARGET_REGEX, Pattern.MULTILINE);

    public String match(CharSequence output) {
        Matcher matcher = COMPILED_PATTERN.matcher(output);
        String matchedString = null;
        while (matcher.find()) {
            matchedString = matcher.group(1);
        }
        return StringUtils.left(matchedString, 30);
    }
}
