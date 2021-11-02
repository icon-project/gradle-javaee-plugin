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

import foundation.icon.gradle.plugins.javaee.task.OptimizedJar;
import org.gradle.api.Project;
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JavaeePluginTest {
    @Test
    public void pluginRegistersATask() {
        // Create a test project and apply the plugin
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("foundation.icon.javaee");

        // Verify the result
        assertNotNull(project.getTasks().findByName(OptimizedJar.getTaskName()));
    }

    @Disabled
    @Test
    public void testWithTo() throws IOException {
        String to = "cx8d1480cd47fb2b60bf32f285820589793a0efbb6";
        String[] testArgs = new String[]{"", "to = '" + to + "'"};
        for (String arg : testArgs) {
            TemporaryFolder testProjectDir = new TemporaryFolder();
            testProjectDir.create();
            var buildFile = testProjectDir.newFile("build.gradle");
            FileWriter writer = new FileWriter(buildFile);
            writer.write(""
                    + "plugins {\n"
                    + "    id 'foundation.icon.javaee'\n"
                    + "}\n"
                    + "deployJar {\n"
                    + "    endpoints {\n"
                    + "        local {\n"
                    + "            uri = 'http://localhost:9082/api/v3'\n"
                    + "            nid = 0x3\n"
                    + "            " + arg + "\n"
                    + "        }\n"
                    + "    }\n"
                    + "}\n"
            );
            writer.close();

            var result = GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("deployToLocal")
                    .withPluginClasspath()
                    .build();
            System.out.println(result.getOutput());
            assertEquals(TaskOutcome.SUCCESS, result.task(":deployToLocal").getOutcome());
        }
    }
}
