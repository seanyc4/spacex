package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.data.cache.LaunchCacheDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.network.LaunchCacheDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchCacheDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsLaunchCacheDataSource(
        impl: LaunchCacheDataSourceImpl
    ): LaunchCacheDataSource
}