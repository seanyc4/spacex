package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.implementation.domain.model.Launch
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import javax.inject.Inject

internal class LaunchNetworkDataSourceImpl @Inject constructor(
    private val api: LaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<Launch> {
        return networkMapper.mapEntityToList(
            api.getLaunchList(options = launchOptions)
        )
    }

}