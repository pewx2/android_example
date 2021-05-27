package ru.example.sed.di.module

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.example.sed.auth.AuthApi
import ru.example.sed.auth.AuthRepository
import ru.example.sed.core.CoreUtils
import ru.example.sed.di.AppScope
import ru.example.sed.service.CacheService

@Module
class AppModule {
    @AppScope
    @Provides
    fun provideCacheService(context: Context): CacheService = CacheService(context)

    @AppScope
    @Provides
    fun provideCoreUtils(moshi: Moshi): CoreUtils = CoreUtils(moshi)

    @AppScope
    @Provides
    fun provideAuthRepository(
        authApi: AuthApi,
        cacheService: CacheService,
        coreUtils: CoreUtils
    ) = AuthRepository(
        authApi,
        cacheService,
        coreUtils
    )

    @AppScope
    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
}