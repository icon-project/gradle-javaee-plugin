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

package foundation.icon.gradle.plugins.javaee.task;

import foundation.icon.ee.tooling.deploy.OptimizedJarBuilder;
import org.gradle.api.file.RegularFile;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.jvm.tasks.Jar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class OptimizedJar extends Jar {
    private static final String TASK_NAME = "optimizedJar";

    private final Property<String> mainClassName;
    private final Property<String> outputJarName;
    private boolean debugMode;

    public static String getTaskName() {
        return TASK_NAME;
    }

    public OptimizedJar() {
        super();
        ObjectFactory objectFactory = getProject().getObjects();
        mainClassName = objectFactory.property(String.class).convention("");
        outputJarName = objectFactory.property(String.class);
        outputJarName.convention(this.getProject().provider(
                () -> getJarFilename(getArchiveFile().get().toString())));
        debugMode = false;
    }

    public void setMainClassName(String mainClassName) {
        this.mainClassName.set(mainClassName);
    }

    public void setEnableDebug(boolean enableDebug) {
        debugMode = enableDebug;
    }

    @Input
    public String getOutputJarName() {
        return this.outputJarName.getOrNull();
    }

    @Override
    protected void copy() {
        configureJarMainClass();
        super.copy();
        runJarOptimizer();
    }

    private void configureJarMainClass() {
        getManifest().attributes(Collections.singletonMap("Main-Class", mainClassName.get()));
    }

    private void runJarOptimizer() {
        var rawJar = getArchiveFile().get();
        byte[] jarBytes = getJarBytes(rawJar);
        OptimizedJarBuilder jarBuilder = new OptimizedJarBuilder(debugMode, jarBytes)
                .withUnreachableMethodRemover()
                .withRenamer();
        byte[] optimizedJar = jarBuilder.getOptimizedBytes();
        writeFile(outputJarName.get(), optimizedJar);
    }

    private String getJarFilename(String input) {
        int len = input.lastIndexOf(".jar");
        String prefix = input.substring(0, len);
        if (debugMode) {
            return prefix + "-debug.jar";
        } else {
            return prefix + "-optimized.jar";
        }
    }

    private byte[] getJarBytes(RegularFile rawJar) {
        try {
            return Files.readAllBytes(rawJar.getAsFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException("JAR read error: " + e.getMessage());
        }
    }

    private static void writeFile(String filePath, byte[] data) {
        Path outFile = Paths.get(filePath);
        try {
            Files.write(outFile, data);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
