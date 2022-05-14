package com.seancoyle.spacex.business.data.network.implementation.launch

import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LaunchNetworkDataSourceImpl
@Inject
constructor(
    private val retrofitService: LaunchRetrofitService
) : LaunchNetworkDataSource {

    override suspend fun getLaunchList(): List<LaunchDomainEntity> {
        return retrofitService.getLaunchList()
    }

}




























