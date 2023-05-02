package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchOptions
import javax.inject.Inject

class LaunchNetworkDataSourceImpl
@Inject
constructor(
    private val api: LaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : com.seancoyle.launch.api.LaunchNetworkDataSource {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel> {
        return networkMapper.mapEntityToList(
            api.getLaunchList(options = launchOptions)
        )
    }

}





























