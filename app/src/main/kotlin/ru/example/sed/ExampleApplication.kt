package ru.example.sed

import android.content.Context
import com.airbnb.mvrx.Mavericks
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import com.google.android.play.core.splitcompat.SplitCompatApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import ru.example.sed.BuildConfig
import ru.example.sed.di.AppComponent
import ru.example.sed.di.DaggerAppComponent
import ru.example.sed.di.module.ContextModule


class ExampleApplication : SplitCompatApplication() {
    lateinit var appComponent: AppComponent

    companion object {
        @JvmStatic
        fun appComponent(context: Context) =
            (context.applicationContext as ExampleApplication).appComponent
    }

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)
        initCoreDependencyInjection()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        if (BuildConfig.DEBUG) {
            initDebugUtils()
        }

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.addPlugin(appComponent.networkFlipperPlugin())
            client.start()
        }

        Mavericks.initialize(this)
    }

    private fun initCoreDependencyInjection() {
        appComponent = DaggerAppComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
    }

    private fun initDebugUtils() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}