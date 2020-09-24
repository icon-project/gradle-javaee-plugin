[![Download](https://api.bintray.com/packages/icon/javaee/gradle-javaee-plugin/images/download.svg)](https://bintray.com/icon/javaee/gradle-javaee-plugin/_latestVersion)

# Gradle plugin for Java Execution Environment

`gradle-javaee-plugin` is a Gradle plugin to automate the process of generating the optimized jar bundle.
The generated jar bundle can be used for deployment to ICON networks that support the Java SCORE execution environment (a.k.a. JavaEE).

## Getting Started

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'foundation.icon:gradle-javaee-plugin:0.7.1'
    }
}

repositories {
    jcenter()
}

apply plugin: 'java'
apply plugin: 'foundation.icon.javaee'
```

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

## License

This project is available under the [Apache License, Version 2.0](LICENSE).
