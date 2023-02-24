package com.seancoyle.launch_datasource.network

import com.seancoyle.launch_models.model.launch.LaunchModel
import com.seancoyle.launch_models.model.launch.LaunchOptions
import javax.inject.Inject

class LaunchNetworkDataSourceImpl
@Inject
constructor(
    private val api: LaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel> {
        return networkMapper.mapEntityToList(
            api.getLaunchList(options = launchOptions)
        )
    }

}





























