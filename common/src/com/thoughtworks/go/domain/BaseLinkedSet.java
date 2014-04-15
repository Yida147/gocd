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

import java.util.LinkedHashSet;
import java.util.List;


public class BaseLinkedSet<T> extends LinkedHashSet<T> {


    public BaseLinkedSet() {

    }

    public BaseLinkedSet(List<T> list) {
        for (T object : list) {
            this.add(object);
        }
    }

    public T first() {
        if (this.size() > 0) {
            return this.iterator().next();
        }
        return null;
    }
}
