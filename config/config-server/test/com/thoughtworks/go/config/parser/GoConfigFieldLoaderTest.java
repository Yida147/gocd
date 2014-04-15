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

package com.thoughtworks.go.config.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GoConfigFieldLoaderTest {

    @Test
    public void shouldOnlyCreateOneInstanceOfSimpleTypeConverterAndUseIt() throws Exception {
        Integer dummyValue = 0;
        Field randomField = dummyValue.getClass().getDeclaredFields()[0]; // Random field because I can't mock java.lang.reflect.Field (Sachin)
        GoConfigFieldLoader loader = GoConfigFieldLoader.fieldParser(null, null, randomField, null, null, new ConfigReferenceElements());
        Field expectedField = loader.getClass().getDeclaredField("typeConverter");
        int modifier = expectedField.getModifiers();
        assertThat(Modifier.isStatic(modifier), is(true));
        assertThat(Modifier.isFinal(modifier), is(true));
    }
}

