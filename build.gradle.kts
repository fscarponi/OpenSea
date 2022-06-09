plugins {
    id("com.android.application") version "7.1.3" apply false
    id("com.android.library") version "7.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.5.31" apply false
}
buildscript {
    val agpVersion by extra("7.4.0-alpha03")
    val kotlinVersion: String by project // command line argument
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.14.2")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.2")
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.5.2")
        classpath("com.google.firebase:perf-plugin:1.4.1")  // Performance Monitoring plugin
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")



    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
