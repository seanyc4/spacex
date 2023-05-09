package com.seancoyle.spacex.framework.datasource.network.launch

import com.seancoyle.launch.api.LaunchNetworkDataSource
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchOptions
import com.seancoyle.launch.implementation.data.network.LaunchNetworkMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeLaunchNetworkDataSourceImpl @Inject constructor(
    private val fakeApi: FakeLaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel> {
        return networkMapper.mapEntityToList(
            fakeApi.getLaunchList(options = launchOptions)
        )
    }
}