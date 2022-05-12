package com.seancoyle.spacex.business.data.network.abstraction.launch

import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity

interface LaunchNetworkDataSource {

    suspend fun getLaunchList(): List<LaunchDomainEntity>

}
