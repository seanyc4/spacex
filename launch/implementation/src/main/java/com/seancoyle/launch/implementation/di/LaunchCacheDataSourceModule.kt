package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.implementation.data.cache.LaunchCacheDataSourceImpl
import com.seancoyle.launch.implementation.data.cache.LaunchEntityMapper
import com.seancoyle.launch.implementation.data.cache.LaunchEntityMapperImpl
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
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

    @Binds
    abstract fun bindsLaunchEntityMapper(
        impl: LaunchEntityMapperImpl
    ): LaunchEntityMapper
}