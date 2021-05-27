package ru.example.sed.di

import android.content.Context
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Component
import retrofit2.Retrofit
import ru.example.sed.auth.AuthApi
import ru.example.sed.auth.AuthRepository
import ru.example.sed.core.CoreUtils
import ru.example.sed.di.module.AppModule
import ru.example.sed.di.module.ContextModule
import ru.example.sed.di.module.NetworkModule
import ru.example.sed.service.CacheService

@AppScope
@Component(
    modules = [
        AppModule::class,
        ContextModule::class,
        NetworkModule::class,
    ],
)
interface AppComponent {
    fun context(): Context
    fun coreUtils(): CoreUtils
    fun retrofit(): Retrofit
    fun networkFlipperPlugin(): NetworkFlipperPlugin

    fun cacheService(): CacheService

    fun authApi(): AuthApi
    fun authRepository(): AuthRepository
}