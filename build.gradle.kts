plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.0"
}

pluginBundle {
    website = "https://github.com/HGuillemet/gradle-javacpp-libextract"
    vcsUrl = "https://github.com/HGuillemet/gradle-javacpp-libextract"
    tags = listOf("javacpp", "jlink", "jpms")
}
gradlePlugin {
    plugins {
        create("libextract") {
            id = "fr.apteryx.javacpp-libextract"
            displayName = "JavaCPP library extracting plugin"
            description = "Extract from the jar dependencies the JavaCPP native libraries used by an application"
            implementationClass = "fr.apteryx.gradle.javacpp.libextract.Plugin"
        }
    }
}

group = "fr.apteryx"
version = "0.1"

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation("org.bytedeco:gradle-javacpp:1.5.8-SNAPSHOT")
    implementation("org.javassist:javassist:3.29.0-GA")
}