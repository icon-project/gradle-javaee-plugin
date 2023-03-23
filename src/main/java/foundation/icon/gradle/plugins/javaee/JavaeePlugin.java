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

import foundation.icon.gradle.plugins.javaee.ext.DeploymentExtension;
import foundation.icon.gradle.plugins.javaee.task.OptimizedJar;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;

public class JavaeePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        if (!project.getPlugins().hasPlugin(JavaPlugin.class)) {
            project.getPlugins().apply(JavaPlugin.class);
        }

        // adding "-parameters" option by default
        project.getTasks().withType(JavaCompile.class).forEach((task) -> {
            var args = task.getOptions().getCompilerArgs();
            if (!args.contains("-parameters")) {
                args.add("-parameters");
            }
        });

        var javaExtension = project.getExtensions().getByType(JavaPluginExtension.class);
        var optJar = project.getTasks().create(OptimizedJar.getTaskName(), OptimizedJar.class);
        optJar.setGroup(BasePlugin.BUILD_GROUP);
        optJar.setDescription("Creates a optimized JAR that can be used for SCORE deployment.");
        optJar.from(javaExtension.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME).getOutput());
        optJar.dependsOn(project.getTasks().findByName(JavaPlugin.JAR_TASK_NAME));

        project.getExtensions().create(DeploymentExtension.getExtName(), DeploymentExtension.class, project);
    }
}
