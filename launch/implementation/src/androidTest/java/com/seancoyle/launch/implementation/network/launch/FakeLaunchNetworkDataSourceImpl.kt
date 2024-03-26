package com.seancoyle.launch.implementation.network.launch

import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.implementation.data.network.LaunchNetworkMapper
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.launch.implementation.domain.network.LaunchNetworkDataSource
import javax.inject.Inject

internal class FakeLaunchNetworkDataSourceImpl @Inject constructor(
    private val fakeApi: FakeLaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    override suspend fun getLaunches(launchOptions: LaunchOptions): List<Launch> {
        return networkMapper.mapEntityToList(
            fakeApi.getLaunches(options = launchOptions)
        )
    }
}