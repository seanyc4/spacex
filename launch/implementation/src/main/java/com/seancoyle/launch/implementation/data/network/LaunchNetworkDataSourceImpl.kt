package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.contract.data.LaunchNetworkDataSource
import com.seancoyle.launch.contract.domain.model.Launch
import com.seancoyle.launch.contract.domain.model.LaunchOptions
import javax.inject.Inject

class LaunchNetworkDataSourceImpl
@Inject
constructor(
    private val api: LaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<Launch> {
        return networkMapper.mapEntityToList(
            api.getLaunchList(options = launchOptions)
        )
    }

}





























