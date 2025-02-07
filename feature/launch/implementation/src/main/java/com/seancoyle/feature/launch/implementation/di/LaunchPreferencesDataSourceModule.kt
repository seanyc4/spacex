package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.data.network.LaunchPreferencesDataSource
import com.seancoyle.feature.launch.implementation.data.cache.LaunchPreferencesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchPreferencesDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsLaunchPreferencesDataSource(
        impl: LaunchPreferencesDataSourceImpl
    ): LaunchPreferencesDataSource
}