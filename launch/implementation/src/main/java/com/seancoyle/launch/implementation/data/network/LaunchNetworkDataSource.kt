package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.implementation.domain.model.LaunchOptions

internal interface LaunchNetworkDataSource {
    suspend fun getLaunchList(launchOptions: LaunchOptions): List<Launch>
}