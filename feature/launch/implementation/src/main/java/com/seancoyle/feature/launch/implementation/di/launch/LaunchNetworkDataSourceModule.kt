package com.seancoyle.feature.launch.implementation.di.launch

import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchNetworkDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchNetworkDataSourceModule {

    @Binds
    abstract fun bindLaunchNetworkDataSource(
        impl: LaunchNetworkDataSourceImpl
    ): LaunchNetworkDataSource
}