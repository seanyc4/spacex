package com.seancoyle.launch_datasource.di.network

import com.seancoyle.launch_datasource.network.LaunchNetworkDataSource
import com.seancoyle.launch_datasource.network.LaunchNetworkDataSourceImpl
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