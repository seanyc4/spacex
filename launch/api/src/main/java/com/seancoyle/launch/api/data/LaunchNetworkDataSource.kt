package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.launch.api.domain.model.ViewModel

interface LaunchNetworkDataSource {
    suspend fun getLaunchList(launchOptions: LaunchOptions): List<ViewModel>
}
