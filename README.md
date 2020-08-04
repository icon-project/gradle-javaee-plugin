# Gradle plugin for JavaEE SDK

`gradle-javaee-plugin` is a Gradle plugin to automate the process of generating the optimized jar bundle.
The generated jar bundle can be used for deployment to ICON networks that support the Java SCORE execution environment (a.k.a. JavaEE).

## Getting Started

```groovy
buildscript {
    repositories {
        jcenter()
        maven {
            url "http://ci.arch.iconloop.com/nexus/repository/maven-public"
        }
    }
    dependencies {
        classpath 'foundation.icon:gradle-javaee-plugin:0.6.3'
    }
}

repositories {
    jcenter()
    maven {
        url "http://ci.arch.iconloop.com/nexus/repository/maven-public"
    }
}

apply plugin: 'java'
apply plugin: 'foundation.icon.javaee-sdk'
```

**[Note]** As of now, `gradle-javaee-plugin` can only be retrieved through the local Nexus repository.
The exact plan when it will be available to the public is not yet decided.

## Configuring Tasks

The `optimizedJar` task type extends from Gradle's `Jar` type.
This means that all attributes and methods available on `Jar` are also available on `optimizedJar`.
Refer the _Gradle User Guide_ for [Jar](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.bundling.Jar.html) for details.

### Specifying a Main Class Name

`mainClassName` property is required to indicate the entry point that is used for the initial execution.
This property will be included into the generated jar bundle with the `Main-Class` header in the manifest.

```groovy
optimizedJar {
    mainClassName = 'com.iconloop.score.example.HelloWorld'
}
```

## Licenses

This project follows the Apache 2.0 License. Please refer to [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) for details.
