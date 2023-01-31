package com.seancoyle.launch_datasource.network

import com.seancoyle.launch_models.model.launch.LaunchModel
import com.seancoyle.launch_models.model.launch.LaunchOptions

interface LaunchNetworkDataSource {

    suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel>

}
