package foundation.icon.gradle.javaee;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * A simple 'hello world' plugin.
 */
public class SDKPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        // Register a task
        project.getTasks().register("greeting", task -> {
            task.doLast(s -> System.out.println("Hello from plugin 'foundation.icon.javaee-sdk'"));
        });
    }
}
