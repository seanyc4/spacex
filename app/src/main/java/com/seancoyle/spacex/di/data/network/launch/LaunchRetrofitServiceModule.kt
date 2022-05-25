package com.seancoyle.spacex.di.data.network.launch

import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import com.seancoyle.spacex.framework.datasource.network.api.launch.LaunchApi
import com.seancoyle.spacex.framework.datasource.network.implementation.launch.LaunchRetrofitServiceImpl
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LaunchRetrofitServiceModule {

    @Singleton
    @Provides
    fun provideLaunchRetrofitService(
        api: LaunchApi,
        networkMapper: LaunchNetworkMapper
    ): LaunchRetrofitService {
        return LaunchRetrofitServiceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }
}