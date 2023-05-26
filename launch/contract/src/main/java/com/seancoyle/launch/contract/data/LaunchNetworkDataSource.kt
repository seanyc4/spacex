package com.seancoyle.launch.contract.data

import com.seancoyle.launch.contract.domain.model.LaunchOptions
import com.seancoyle.launch.contract.domain.model.ViewModel

interface LaunchNetworkDataSource {
    suspend fun getLaunchList(launchOptions: LaunchOptions): List<ViewModel>
}
