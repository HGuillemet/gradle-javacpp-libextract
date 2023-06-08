plugins {
    id("com.gradle.plugin-publish") version "1.2.0"
}

group = "fr.apteryx"
version = "0.4"

gradlePlugin {
    website.set("https://github.com/HGuillemet/gradle-javacpp-libextract")
    vcsUrl.set("https://github.com/HGuillemet/gradle-javacpp-libextract")
    plugins {
        create("libextract") {
            id = "fr.apteryx.javacpp-libextract"
            displayName = "JavaCPP library extracting plugin"
            description = "Extract from the jar dependencies the JavaCPP native libraries used by an application"
            implementationClass = "fr.apteryx.gradle.javacpp.libextract.Plugin"
            tags.set(listOf("javacpp", "jlink", "jpms"))
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

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
    implementation("org.bytedeco:gradle-javacpp:1.5.9")
    implementation("org.javassist:javassist:3.29.2-GA")
}
