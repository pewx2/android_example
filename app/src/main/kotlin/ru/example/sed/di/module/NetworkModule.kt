package ru.example.sed.di.module

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.example.sed.BuildConfig
import ru.example.sed.core.ServerSelectionInterceptor
import ru.example.sed.di.AppScope
import ru.example.sed.service.CacheService
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {
    @AppScope
    @Provides
    fun hostSelectionInterceptor(cacheService: CacheService): ServerSelectionInterceptor {
        return ServerSelectionInterceptor(cacheService)
    }

    @AppScope
    @Provides
    fun provideHttpClient(
        serverSelectionInterceptor: ServerSelectionInterceptor,
        networkFlipperPlugin: NetworkFlipperPlugin
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()

        clientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        clientBuilder.readTimeout(30, TimeUnit.SECONDS)

        clientBuilder.addInterceptor(serverSelectionInterceptor)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
        }

        return clientBuilder.build()
    }

    @AppScope
    @Provides
    fun provideMoshiBuilder(): Moshi = Moshi.Builder().build()

    @AppScope
    @Provides
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.SED_API_SERVER)
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory
                    .create(moshi)
                    .withNullSerialization()
            )
            .build()

    @AppScope
    @Provides
    fun networkFlipperPlugin() = NetworkFlipperPlugin()
}