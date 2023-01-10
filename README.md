## JavaCPP libextract plugin ##

JavaCPP presets are distributed with native libraries bundled in jar files.
JavaCPP application can directly run with these jar files in classpath or modulepath.
The JavaCPP loader takes care of extracting the native libraries into a cache directory
(`$HOME/.javacpp/cache` if not configured otherwise), before loading them.

While this is practical during development, it is not the best solution when 
run by the end user: the libraries should preferably be installed 
in the installation directory instead of silently extracted into a hidden directory 
of the user home that will never be cleaned.
Also the jar files could contain libraries that are actually never used by the
application.

This Gradle plugin defines a task type, named `ExtractLibraries`. Its action 
automatically populates a directory
with the libraries used by the application. It relies on Javassist to
determine the JavaCPP classes the applications depends on and extract only
the necessary native libraries. An extraction can be performed for each source set.
The plugin exposes a container extension called `libraryExtractions` 
which must contain one configuration for each source set.
The configurable options are the target directory (mandatory) and 
whether the directory must be cleared before being populated or not (default is false).
The `gradle-javacpp-platform` plugin is automatically applied in order to limit the dependencies
to the host platform.
For each of such configuration, a task called `<sourceSet>ExtractLibraries`
(e.g.`mainExtractLibraries`) is created.

Examples are shown below:

Groovy DSL:
```groovy
plugins {
    id 'application'
    id 'fr.apteryx.javacpp-libextract' version '0.3'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.bytedeco:opencv-platform:4.6.0-1.5.8'
}

libraryExtractions {
  create('main') {
    targetDirectory = layout.buildDirectory.dir('native')
    clearTargetDirectory = true  
  }
}
```

Kotlin DSL:
```kotlin
plugins {
    application
    id("fr.apteryx.javacpp-libextract") version "0.3"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.bytedeco:opencv-platform:4.6.0-1.5.8")
}

libraryExtractions {
  create("main") {
    targetDirectory.set(layout.buildDirectory.dir("native"))
  }
}
```

Users must be aware of two limitations:
1. Since the plugin actually load the needed library to trigger extractions, only the extraction of host platform libraries is supported.
2. Due to the impossibility to voluntarily unload JNI libraries in Java, this plugin cannot run in Gradle daemon or the library will stay loaded after task executions. This would have side effects like the creation of duplicate libraries in target directory in a attempt of JavaCPP to load a library that is already loaded, or the impossibility to delete a library file on Windows. Daemon mode can be disabled using the `--no-daemon` Gradle command line option, or by adding `org.gradle.daemon=false` to your `gradle.properties` file.

NOTE: This plugin is transparently used by another plugin,
the JavaCPP jlink plugin, for the specific use case of building jlink image
and jpackage bundles for distribution.
