package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.data.network.LaunchNetworkDataSourceImpl
import com.seancoyle.feature.launch.implementation.domain.network.LaunchNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchNetworkDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsLaunchNetworkDataSource(
        impl: LaunchNetworkDataSourceImpl
    ): LaunchNetworkDataSource
}