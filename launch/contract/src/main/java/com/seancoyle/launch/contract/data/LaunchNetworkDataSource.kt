package com.seancoyle.launch.contract.data

import com.seancoyle.launch.contract.domain.model.Launch
import com.seancoyle.launch.contract.domain.model.LaunchOptions

interface LaunchNetworkDataSource {
    suspend fun getLaunchList(launchOptions: LaunchOptions): List<Launch>
}
