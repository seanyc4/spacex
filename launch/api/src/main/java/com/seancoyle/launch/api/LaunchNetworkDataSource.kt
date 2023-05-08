package com.seancoyle.launch.api

import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchOptions

interface LaunchNetworkDataSource {
    suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel>
}
