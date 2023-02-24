package com.seancoyle.launch_datasource.di.cache

import com.seancoyle.launch_datasource.cache.LaunchCacheDataSource
import com.seancoyle.launch_datasource.cache.LaunchCacheDataSourceImpl
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