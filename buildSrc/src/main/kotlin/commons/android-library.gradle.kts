package commons

import BuildAndroidConfig
import dependencies.AnnotationProcessorsDependencies
import dependencies.Dependencies
import extensions.implementation
import extensions.kapt

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(BuildAndroidConfig.COMPILE_SDK_VERSION)

    defaultConfig {
        minSdkVersion(BuildAndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(BuildAndroidConfig.TARGET_SDK_VERSION)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
    }
}



dependencies {
    implementation(Dependencies.DAGGER)
    implementation(Dependencies.LOGGER)

    kapt(AnnotationProcessorsDependencies.DAGGER)
}
