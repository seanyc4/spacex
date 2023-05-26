package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.contract.data.LaunchNetworkDataSource
import com.seancoyle.launch.implementation.data.network.LaunchNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LaunchNetworkDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsLaunchNetworkDataSource(
        impl: LaunchNetworkDataSourceImpl
    ): LaunchNetworkDataSource
}