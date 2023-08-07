package com.seancoyle.spacex.di.data.launch

import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.implementation.data.network.LaunchNetworkMapper
import com.seancoyle.launch.implementation.di.LaunchNetworkDataSourceModule
import com.seancoyle.spacex.data.network.launch.FakeLaunchApi
import com.seancoyle.spacex.data.network.launch.FakeLaunchNetworkDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LaunchNetworkDataSourceModule::class]
)
object TestLaunchNetworkDataSourceModule {

    @Singleton
    @Provides
    fun provideLaunchNetworkDataSource(
        fakeApi: FakeLaunchApi,
        networkMapper: LaunchNetworkMapper
    ): LaunchNetworkDataSource {
        return FakeLaunchNetworkDataSourceImpl(
            fakeApi = fakeApi,
            networkMapper = networkMapper
        )
    }

}