package com.seancoyle.spacex.framework.datasource.network.abstraction.launch

import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions

interface LaunchRetrofitService {

    suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchDomainEntity>

}