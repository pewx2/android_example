package commons

import BuildAndroidConfig
import BuildModules
import dependencies.AnnotationProcessorsDependencies
import dependencies.Dependencies
import extensions.kapt

plugins {
    id("com.android.dynamic-feature")
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

    buildFeatures.viewBinding = true

    flavorDimensions("region")
    productFlavors {
        create("generic") {
            dimension = "region"
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
    }
}

dependencies {
    implementation(project(BuildModules.APP))
    implementation(project(BuildModules.Commons.UI))

    api(Dependencies.DAGGER)
    api(Dependencies.LOGGER)
    api(Dependencies.CONSTRAINT_LAYOUT)
    api(Dependencies.RETROFIT)
    api(Dependencies.FIREBASE_CRASHLYTICS)
    api(Dependencies.NAVIGATION_FRAGMENT)
    api(Dependencies.NAVIGATION_UI)

    kapt(AnnotationProcessorsDependencies.DAGGER)
}
