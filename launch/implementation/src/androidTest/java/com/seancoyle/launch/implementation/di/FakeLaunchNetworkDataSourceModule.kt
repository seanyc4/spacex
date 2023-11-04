package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.implementation.network.launch.FakeLaunchNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LaunchNetworkDataSourceModule::class]
)
internal abstract class FakeLaunchNetworkDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsFakeLaunchNetworkDataSource(
        impl: FakeLaunchNetworkDataSourceImpl
    ): LaunchNetworkDataSource
}