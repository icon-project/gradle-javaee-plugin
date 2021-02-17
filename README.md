[![Maven Central](https://maven-badges.herokuapp.com/maven-central/foundation.icon/gradle-javaee-plugin/badge.svg)](https://search.maven.org/search?q=g:foundation.icon%20a:gradle-javaee-plugin)

# Gradle plugin for Java Execution Environment

`gradle-javaee-plugin` is a Gradle plugin to automate the process of generating the optimized jar bundle.
The generated jar bundle can be used for deployment to ICON networks that support the Java SCORE execution environment (a.k.a. JavaEE).

## Getting Started

```groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url 'https://oss.jfrog.org/artifactory/oss-snapshot-local'
        }
    }
    dependencies {
        classpath 'foundation.icon:gradle-javaee-plugin:0.7.5'
    }
}

repositories {
    mavenCentral()
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

### Jar Deployment

The `deployJar` extension can be used to deploy the optimized jar to local or remote ICON networks that support the Java SCORE execution environment.

```groovy
deployJar {
    endpoints {
        local {
            uri = 'http://localhost:9082/api/v3'
            nid = 3
        }
    }
    keystore = './mykey.json'
    password = 'keypass'
    parameters {
        arg('name', 'Alice')
    }
}
```

The above extension creates `deployToLocal` task automatically based on the container name of the given endpoints property.

## Examples

- [Java SCORE Examples](https://github.com/icon-project/java-score-examples)

## License

This project is available under the [Apache License, Version 2.0](LICENSE).
