package com.seancoyle.launch_datasource.di.network

import com.seancoyle.launch_datasource.network.LaunchApi
import com.seancoyle.launch_datasource.network.LaunchNetworkDataSource
import com.seancoyle.launch_datasource.network.LaunchNetworkDataSourceImpl
import com.seancoyle.launch_datasource.network.LaunchNetworkMapper
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