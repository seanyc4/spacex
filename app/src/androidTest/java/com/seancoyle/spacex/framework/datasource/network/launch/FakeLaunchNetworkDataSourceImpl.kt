package com.seancoyle.spacex.framework.datasource.network.launch

import com.seancoyle.launch_datasource.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.launch_datasource.network.mappers.launch.LaunchNetworkMapper
import com.seancoyle.launch_models.model.launch.LaunchModel
import com.seancoyle.launch_models.model.launch.LaunchOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeLaunchNetworkDataSourceImpl
    @Inject
    constructor(
        private val fakeApi: FakeLaunchApi,
        private val networkMapper: LaunchNetworkMapper
    ) : LaunchNetworkDataSource {

        override suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel> {
            return networkMapper.mapEntityToList(
                fakeApi.getLaunchList(options = launchOptions)
            )
        }

    }