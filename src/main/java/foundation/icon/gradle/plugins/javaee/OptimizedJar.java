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

package foundation.icon.gradle.plugins.javaee;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.jvm.tasks.Jar;

import java.util.Collections;

public class OptimizedJar extends Jar {
    private static final String TASK_NAME = "optimizedJar";

    private final Property<String> mainClassName;

    public static String getTaskName() {
        return TASK_NAME;
    }

    public OptimizedJar() {
        super();
        ObjectFactory objectFactory = getProject().getObjects();
        mainClassName = objectFactory.property(String.class).convention("");
    }

    public void setMainClassName(String mainClassName) {
        this.mainClassName.set(mainClassName);
    }

    @Override
    protected void copy() {
        getLogger().info("=== Hello: " + TASK_NAME + " ===");
        configureJarMainClass();
        super.copy();
        getLogger().info("=== END: " + TASK_NAME + " ===");
    }

    private void configureJarMainClass() {
        getManifest().attributes(Collections.singletonMap("Main-Class", mainClassName.get()));
    }
}
