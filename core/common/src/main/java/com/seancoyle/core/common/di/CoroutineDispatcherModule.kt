package com.seancoyle.core.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

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

    @DefaultDispatcher
    @Provides
    fun provideCoroutineDispatcherDefault(): CoroutineDispatcher =
        Dispatchers.Default

    @Provides
    @Singleton
    @ApplicationScope
    fun providesCoroutineScope(
        @IODispatcher dispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}