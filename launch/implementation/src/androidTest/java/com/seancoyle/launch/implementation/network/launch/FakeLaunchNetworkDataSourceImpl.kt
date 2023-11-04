package com.seancoyle.launch.implementation.network.launch

import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.launch.implementation.data.network.LaunchNetworkMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FakeLaunchNetworkDataSourceImpl @Inject constructor(
    private val fakeApi: FakeLaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<Launch> {
        return networkMapper.mapEntityToList(
            fakeApi.getLaunchList(options = launchOptions)
        )
    }
}