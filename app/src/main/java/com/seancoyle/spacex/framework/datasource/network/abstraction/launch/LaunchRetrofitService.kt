package com.seancoyle.spacex.framework.datasource.network.abstraction.launch

import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions

interface LaunchRetrofitService {

    suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel>

}