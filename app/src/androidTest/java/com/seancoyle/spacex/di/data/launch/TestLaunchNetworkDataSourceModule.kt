package com.seancoyle.spacex.di.data.launch

import com.seancoyle.launch_datasource.di.network.LaunchNetworkDataSourceModule
import com.seancoyle.launch_datasource.network.LaunchNetworkDataSource
import com.seancoyle.launch_datasource.network.LaunchNetworkMapper
import com.seancoyle.spacex.framework.datasource.network.launch.FakeLaunchApi
import com.seancoyle.spacex.framework.datasource.network.launch.FakeLaunchNetworkDataSourceImpl
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