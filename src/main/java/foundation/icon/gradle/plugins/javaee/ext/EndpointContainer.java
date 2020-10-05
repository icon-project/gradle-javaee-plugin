/*
 * Copyright 2020 ICON Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package foundation.icon.gradle.plugins.javaee.ext;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

public class EndpointContainer {
    private final String name;
    private final Property<String> uri;
    private final Property<Integer> nid;

    public EndpointContainer(String name, ObjectFactory objectFactory) {
        this.name = name;
        this.uri = objectFactory.property(String.class);
        this.nid = objectFactory.property(Integer.class);
    }

    public String getName() {
        return name;
    }

    public Property<String> getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri.set(uri);
    }

    public Property<Integer> getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid.set(nid);
    }
}
