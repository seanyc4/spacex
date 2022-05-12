package com.seancoyle.spacex.framework.datasource.network.abstraction.launch

import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity

interface LaunchRetrofitService {

    suspend fun getLaunchList(): List<LaunchDomainEntity>

}