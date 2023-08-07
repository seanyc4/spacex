package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchOptions

interface LaunchNetworkDataSource {
    suspend fun getLaunchList(launchOptions: LaunchOptions): List<Launch>

}