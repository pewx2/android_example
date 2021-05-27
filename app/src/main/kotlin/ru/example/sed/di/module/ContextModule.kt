package ru.example.sed.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.example.sed.di.AppScope


@Module
class ContextModule(private val application: Application) {
    @AppScope
    @Provides
    fun provideContext(): Context = application
}
