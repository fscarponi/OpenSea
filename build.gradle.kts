// Root build.gradle.kts
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    // Empty root plugins block
}

group = "it.fscarponi"
version = "1.0-SNAPSHOT"

// Configure all subprojects
subprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
