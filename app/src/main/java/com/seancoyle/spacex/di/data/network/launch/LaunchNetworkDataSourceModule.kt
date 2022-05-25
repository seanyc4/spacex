package com.seancoyle.spacex.di.data.network.launch

import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.business.data.network.implementation.launch.LaunchNetworkDataSourceImpl
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
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
        launchRetrofitService: LaunchRetrofitService
    ): LaunchNetworkDataSource {
        return LaunchNetworkDataSourceImpl(
            retrofitService = launchRetrofitService
        )
    }
}