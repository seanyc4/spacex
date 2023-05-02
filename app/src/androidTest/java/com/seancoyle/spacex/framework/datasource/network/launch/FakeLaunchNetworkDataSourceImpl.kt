package com.seancoyle.spacex.framework.datasource.network.launch

import com.seancoyle.launch.api.LaunchModel
import com.seancoyle.launch.api.LaunchOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeLaunchNetworkDataSourceImpl
    @Inject
    constructor(
        private val fakeApi: FakeLaunchApi,
        private val networkMapper: com.seancoyle.launch.implementation.data.network.LaunchNetworkMapper
    ) : com.seancoyle.launch.api.LaunchNetworkDataSource {

        override suspend fun getLaunchList(launchOptions: com.seancoyle.launch.api.LaunchOptions): List<com.seancoyle.launch.api.LaunchModel> {
            return networkMapper.mapEntityToList(
                fakeApi.getLaunchList(options = launchOptions)
            )
        }

    }