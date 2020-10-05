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

import foundation.icon.gradle.plugins.javaee.task.DeployJar;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

import java.util.ArrayList;

public class DeploymentExtension {
    private static final String EXTENSION_NAME = "deployJar";

    private final Property<String> keystore;
    private final Property<String> password;
    private final Parameter parameter;
    private final NamedDomainObjectContainer<EndpointContainer> endpointContainer;

    public static String getExtName() {
        return EXTENSION_NAME;
    }

    public DeploymentExtension(Project project) {
        keystore = project.getObjects().property(String.class);
        password = project.getObjects().property(String.class);
        parameter = project.getObjects().newInstance(Parameter.class);
        endpointContainer = project.container(EndpointContainer.class,
                name -> new EndpointContainer(name, project.getObjects()));

        endpointContainer.all(container -> {
            String endpoint = container.getName();
            String capitalizedTarget = endpoint.substring(0, 1).toUpperCase() + endpoint.substring(1);
            String taskName = "deployTo" + capitalizedTarget;
            project.getTasks().register(taskName, DeployJar.class, task -> {
                task.getUri().set(container.getUri());
                task.getNid().set(container.getNid());
            });
            var deployTask = project.getTasks().getByName(taskName);
            deployTask.setGroup("Deployment");
            deployTask.setDescription("Deploys the optimized JAR to " + capitalizedTarget + ".");
        });
    }

    public Property<String> getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore.set(keystore);
    }

    public Property<String> getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public ArrayList<Argument> getArguments() {
        return this.parameter.getArgs();
    }

    public void parameters(Action<? super Parameter> action) {
        action.execute(parameter);
    }

    public void endpoints(Action<? super NamedDomainObjectContainer<EndpointContainer>> action) {
        action.execute(endpointContainer);
    }
}
