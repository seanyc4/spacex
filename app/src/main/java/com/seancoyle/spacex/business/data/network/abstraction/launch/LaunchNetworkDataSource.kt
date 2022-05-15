package com.seancoyle.spacex.business.data.network.abstraction.launch

import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions

interface LaunchNetworkDataSource {

    suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchDomainEntity>

}
