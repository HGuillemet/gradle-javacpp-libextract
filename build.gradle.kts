plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.18.0"
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

publishing {
    repositories {
        mavenLocal()
    }
}

group = "fr.apteryx"
version = "0.2"

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven {
        url=uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencies {
    implementation(gradleApi())
    implementation("org.bytedeco:gradle-javacpp:1.5.8-SNAPSHOT")
    implementation("org.javassist:javassist:3.29.0-GA")
}
