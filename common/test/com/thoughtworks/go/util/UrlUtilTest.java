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

package com.thoughtworks.go.util;

import org.apache.commons.httpclient.URIException;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UrlUtilTest {

    @Test
    public void shouldEncodeUrl() {
        assertThat(UrlUtil.encodeInUtf8("a%b"), is("a%25b"));
    }

    @Test
    public void shouldEncodeAllPartsInUrl() {
        assertThat(UrlUtil.encodeInUtf8("a%b/c%d"), is("a%25b/c%25d"));
    }

    @Test
    public void shouldKeepPrecedingSlash() {
        assertThat(UrlUtil.encodeInUtf8("/a%b/c%d"), is("/a%25b/c%25d"));
    }

    @Test
    public void shouldKeepTrailingSlash() {
        assertThat(UrlUtil.encodeInUtf8("a%b/c%d/"), is("a%25b/c%25d/"));
    }

    @Test
    public void shouldAppendQueryString() throws URIException {
        assertThat(UrlUtil.urlWithQuery("http://baz.quux", "foo", "bar"), is("http://baz.quux?foo=bar"));
        assertThat(UrlUtil.urlWithQuery("http://baz.quux?bang=boom&hello=world", "foo", "bar"), is("http://baz.quux/?bang=boom&hello=world&foo=bar"));
        assertThat(UrlUtil.urlWithQuery("http://baz.quux:1000/hello/world?bang=boom", "foo", "bar"), is("http://baz.quux:1000/hello/world?bang=boom&foo=bar"));
        assertThat(UrlUtil.urlWithQuery("http://baz.quux:1000/hello/world?bang=boom%20bang&quux=bar/baz&sha1=2jmj7l5rSw0yVb%2FvlWAYkK%2FYBwk%3D", "foo", "bar\\baz"), is("http://baz.quux:1000/hello/world?bang=boom+bang&quux=bar%2Fbaz&sha1=2jmj7l5rSw0yVb%2FvlWAYkK%2FYBwk%3D&foo=bar%5Cbaz"));
        assertThat(UrlUtil.urlWithQuery("http://baz.quux:1000/hello/world?bang=boom#in_hell", "foo", "bar"), is("http://baz.quux:1000/hello/world?bang=boom&foo=bar"));//fragment should not be sent to the server(commons-http constraint)
        assertThat(UrlUtil.urlWithQuery("http://user:loser@baz.quux:1000/hello/world#in_hell", "foo", "bar"), is("http://user:loser@baz.quux:1000/hello/world?foo=bar"));
    }
}
