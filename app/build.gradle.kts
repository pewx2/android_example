import dependencies.AnnotationProcessorsDependencies
import dependencies.Dependencies
import extensions.buildConfigStringField
import extensions.getLocalProperty
import extensions.implementation

plugins {
    id(BuildPlugins.ANDROID_APPLICATION)
    id(BuildPlugins.KOTLIN_ANDROID)
    id(BuildPlugins.KOTLIN_KAPT)
    id(BuildPlugins.SAFE_ARGS)
    id(BuildPlugins.PLAY)
    id(BuildPlugins.GOOGLE)
    id(BuildPlugins.CRASHLITICS)
}

play {
    enabled.set(false)
    artifactDir.set(file("build/outputs/bundle/genericRelease/"))

    if (System.getenv("ANDROID_PUBLISHER_CREDENTIALS") != null) {
        enabled.set(true)
    }
}

android {
    compileSdkVersion(BuildAndroidConfig.COMPILE_SDK_VERSION)
    defaultConfig {
        applicationId = BuildAndroidConfig.APPLICATION_ID
        minSdkVersion(BuildAndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(BuildAndroidConfig.TARGET_SDK_VERSION)
        buildToolsVersion(BuildAndroidConfig.BUILD_TOOLS_VERSION)

        versionCode = 1
        versionName = "1"
    }

    signingConfigs {
        create("release") {
            storeFile = if (System.getenv("PATH_TO_KEYSTORE") != null) {
                file(System.getenv("PATH_TO_KEYSTORE"))
            } else {
                file("../signature.jks")
            }

            storePassword = System.getenv("SIGNING_KEYSTORE_PASSWORD")
                ?: getLocalProperty("storePassword")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                ?: getLocalProperty("keyAlias")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
                ?: getLocalProperty("keyPassword")
            isV1SigningEnabled = true
            isV2SigningEnabled = true
        }
    }

    buildTypes {
        getByName("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isMinifyEnabled = false //TODO: BuildTypeRelease.isMinifyEnabled
        }

        getByName("debug") {
            versionNameSuffix = BuildTypeDebug.versionNameSuffix
            isMinifyEnabled = BuildTypeDebug.isMinifyEnabled
        }
    }

    buildTypes.forEach {
        try {
            it.buildConfigStringField(
                "SED_API_SERVER",
                System.getenv("SED_API_SERVER") ?: getLocalProperty("sed.api.server")
            )
        } catch (ignored: Exception) {
            throw InvalidUserCodeException("You should define `sed.api.server` in local.properties")
        }
    }


    flavorDimensions("region")
    productFlavors {
        create("generic") {
            versionNameSuffix = "-generic"
            dimension = "region"
            signingConfig = signingConfigs.getByName("release")

            buildConfigField("boolean", "TLS_ENABLED", "true")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
    }

    dynamicFeatures = mutableSetOf(
        BuildModules.Features.AUTH
    )
}

dependencies {
    implementation(project(BuildModules.Commons.UI))

    implementation(Dependencies.COROUTINES_ANDROID)
    implementation(Dependencies.DAGGER)

    //navigation
    implementation(Dependencies.NAVIGATION_UI)
    implementation(Dependencies.NAVIGATION_FRAGMENT)
    implementation(Dependencies.NAVIGATION_DYNAMIC)

    //network
    implementation(Dependencies.RETROFIT) {
        exclude(group = "com.squareup.okhttp3")
    }
    implementation(Dependencies.RETROFIT_CONVERTER)
    implementation(Dependencies.MOSHI)
    implementation(Dependencies.OKHTTP)
    implementation(Dependencies.FETCH2)
    implementation(Dependencies.FETCH2_OKHTTP)

    //utils
    api(Dependencies.MVRX)
    implementation(Dependencies.LOGGER)
    implementation(Dependencies.FIREBASE_ANALYTICS)
    implementation(Dependencies.FIREBASE_CRASHLYTICS) {
        exclude(group = "com.squareup.okhttp3")
    }

    kapt(AnnotationProcessorsDependencies.DAGGER)
    kapt(AnnotationProcessorsDependencies.MOSHI)

//    Uncomment to find memory leaks
//    debugImplementation(Dependencies.LEAK_CANARY)
    debugImplementation(Dependencies.FLIPPER)
    debugImplementation(Dependencies.FLIPPER_NETWORK)
    debugImplementation(Dependencies.SOLOADER)
    debugImplementation(Dependencies.PLAY_CORE)

    releaseImplementation(Dependencies.FLIPPER_NOOP)
}
