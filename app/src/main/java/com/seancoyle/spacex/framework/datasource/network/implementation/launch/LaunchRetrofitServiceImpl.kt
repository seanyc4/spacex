package com.seancoyle.spacex.framework.datasource.network.implementation.launch

import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
import com.seancoyle.spacex.framework.datasource.network.api.launch.LaunchApi
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchRetrofitServiceImpl
@Inject
constructor(
    private val api: LaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchRetrofitService {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel> {
        return networkMapper.mapEntityToList(
            api.getLaunchList(options = launchOptions)
        )
    }

}












