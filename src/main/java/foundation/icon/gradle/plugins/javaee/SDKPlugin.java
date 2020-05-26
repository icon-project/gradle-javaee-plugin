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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;

public class SDKPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        if (!project.getPlugins().hasPlugin(JavaPlugin.class)) {
            project.getPlugins().apply(JavaPlugin.class);
        }
        var convention = project.getConvention().getPlugin(JavaPluginConvention.class);
        var optJar = project.getTasks().create(OptimizedJar.getTaskName(), OptimizedJar.class);
        optJar.setGroup("javaee-sdk");
        optJar.setDescription("Create a optimized JAR for SCORE deployment");
        optJar.dependsOn(project.getTasks().findByName(JavaPlugin.JAR_TASK_NAME));
        //optJar.from(convention.getSourceSets().getByName("main").getOutput());
    }
}
