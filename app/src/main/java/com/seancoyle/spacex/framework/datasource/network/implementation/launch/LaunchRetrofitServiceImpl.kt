package com.seancoyle.spacex.framework.datasource.network.implementation.launch

import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
import com.seancoyle.spacex.framework.datasource.network.api.launch.LaunchService
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchRetrofitServiceImpl
@Inject
constructor(
    private val service: LaunchService,
    private val networkMapper: LaunchNetworkMapper
) : LaunchRetrofitService {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchDomainEntity> {
        return networkMapper.mapEntityToList(
            service.getLaunchList(options = launchOptions)
        )
    }

}












