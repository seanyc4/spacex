package com.seancoyle.launch_datasource.network.abstraction.launch

import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.launch_domain.model.launch.LaunchOptions

interface LaunchNetworkDataSource {

    suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel>

}
