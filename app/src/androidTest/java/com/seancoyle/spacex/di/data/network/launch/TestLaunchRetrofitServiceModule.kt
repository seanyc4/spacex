package com.seancoyle.spacex.di.data.network.launch

import com.seancoyle.spacex.framework.datasource.api.launch.FakeLaunchApi
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import com.seancoyle.spacex.framework.datasource.network.launch.FakeLaunchRetrofitServiceImpl
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LaunchRetrofitServiceModule::class]
)
object TestLaunchRetrofitServiceModule {

    @Singleton
    @Provides
    fun provideLaunchRetrofitService(
        api: FakeLaunchApi,
        networkMapper: LaunchNetworkMapper
    ): LaunchRetrofitService {
        return FakeLaunchRetrofitServiceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }

}