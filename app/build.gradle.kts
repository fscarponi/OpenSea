plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 32
    namespace = "it.fscarponi.opensea"
    defaultConfig {
        applicationId = "it.fscarponi.opensea"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

        }
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

        }
    }
    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = false
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
val mapForgeVersion: String by project
val ktor_version: String by project
dependencies {


    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.ui:ui-tooling:1.2.0-beta03")
    implementation("androidx.compose.material3:material3:1.0.0-alpha13")
    implementation("androidx.compose.material3:material3-window-size-class:1.0.0-alpha13")
    implementation("androidx.compose.material3:material3:1.0.0-alpha13")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    testImplementation("androidx.compose.ui:ui-test:1.2.0-beta03")

    implementation("androidx.navigation:navigation-compose:2.4.2")


    // maps
    implementation("org.mapsforge:mapsforge-core:$mapForgeVersion")
    implementation("org.mapsforge:mapsforge-map:$mapForgeVersion")
    implementation("org.mapsforge:mapsforge-map-reader:$mapForgeVersion")
    implementation("org.mapsforge:mapsforge-themes:$mapForgeVersion")
    implementation("net.sf.kxml:kxml2:2.3.0")

    implementation ("ovh.plrapps:mapcompose:2.1.0")

    // PEKO (permissions)
    implementation("com.markodevcic:peko:2.1.3")

    //kodein
    implementation("org.kodein.di:kodein-di-framework-compose:7.9.0")
    //KTOR
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

}