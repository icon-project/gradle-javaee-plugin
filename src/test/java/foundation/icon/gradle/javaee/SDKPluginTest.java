package foundation.icon.gradle.javaee;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A simple unit test for the 'foundation.icon.javaee-sdk' plugin.
 */
public class SDKPluginTest {
    @Test public void pluginRegistersATask() {
        // Create a test project and apply the plugin
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("foundation.icon.javaee-sdk");

        // Verify the result
        assertNotNull(project.getTasks().findByName("greeting"));
    }
}
