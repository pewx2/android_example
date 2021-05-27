package dependencies

object Dependencies {
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_CONVERTER = "com.squareup.retrofit2:converter-moshi:${Versions.RETROFIT}"
    const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val MOSHI = "com.squareup.moshi:moshi:${Versions.MOSHI}"
    const val LOGGER = "com.orhanobut:logger:${Versions.LOGGER}"
    const val DAGGER = "com.google.dagger:dagger:${Versions.DAGGER}"
    const val MVRX = "com.airbnb.android:mavericks:${Versions.MVRX}"
    const val PLAY_CORE = "com.google.android.play:core:${Versions.PLAY_CORE}"
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val NAVIGATION_FRAGMENT =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_DYNAMIC =
        "androidx.navigation:navigation-dynamic-features-fragment:${Versions.NAVIGATION}"
    const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics:${Versions.FIREBASE}"
    const val FIREBASE_CRASHLYTICS =
        "com.google.firebase:firebase-crashlytics:${Versions.CRASHLYTICS}"
    const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:${Versions.LEAK_CANARY}"
    const val FETCH2 = "androidx.tonyodev.fetch2:xfetch2:${Versions.FETCH2}"
    const val FETCH2_OKHTTP = "androidx.tonyodev.fetch2okhttp:xfetch2okhttp:${Versions.FETCH2}"

    const val FLIPPER = "com.facebook.flipper:flipper:${Versions.FLIPPER}"
    const val SOLOADER = "com.facebook.soloader:soloader:${Versions.SOLOADER}"
    const val FLIPPER_NOOP = "com.github.theGlenn:flipper-android-no-op:${Versions.FLIPPER_NOOP}"
    const val FLIPPER_NETWORK = "com.facebook.flipper:flipper-network-plugin:${Versions.FLIPPER}"

}