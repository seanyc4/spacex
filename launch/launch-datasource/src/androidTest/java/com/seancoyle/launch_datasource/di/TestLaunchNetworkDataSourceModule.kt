package com.seancoyle.launch_datasource.di

import com.seancoyle.launch_datasource.di.network.launch.LaunchApiModule
import com.seancoyle.launch_datasource.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.launch_datasource.network.launch.FakeLaunchApi
import com.seancoyle.launch_datasource.network.launch.FakeLaunchNetworkDataSourceImpl
import com.seancoyle.launch_datasource.network.mappers.launch.LaunchNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LaunchApiModule::class]
)
object TestLaunchNetworkDataSourceModule {

    @Singleton
    @Provides
    fun provideLaunchNetworkDataSource(
        api: FakeLaunchApi,
        networkMapper: LaunchNetworkMapper
    ): LaunchNetworkDataSource {
        return FakeLaunchNetworkDataSourceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }

}