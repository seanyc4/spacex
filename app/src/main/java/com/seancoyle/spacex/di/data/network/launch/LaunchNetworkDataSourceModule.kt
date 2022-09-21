package com.seancoyle.spacex.di.data.network.launch

import com.seancoyle.launch_datasource.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.launch_datasource.network.api.launch.LaunchApi
import com.seancoyle.launch_datasource.network.implementation.launch.LaunchNetworkDataSourceImpl
import com.seancoyle.launch_datasource.network.mappers.launch.LaunchNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LaunchNetworkDataSourceModule {

    @Singleton
    @Provides
    fun provideLaunchNetworkDataSource(
        api: LaunchApi,
        networkMapper: LaunchNetworkMapper
    ): LaunchNetworkDataSource {
        return LaunchNetworkDataSourceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }
}