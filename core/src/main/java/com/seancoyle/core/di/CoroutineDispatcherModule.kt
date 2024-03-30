package com.seancoyle.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@InstallIn(SingletonComponent::class)
@Module
object CoroutineDispatcherModule {

    @IODispatcher
    @Provides
    fun provideCoroutineDispatcherIO(): CoroutineDispatcher =
        Dispatchers.IO

    @MainDispatcher
    @Provides
    fun provideCoroutineDispatcherMain(): CoroutineDispatcher =
        Dispatchers.Main
}