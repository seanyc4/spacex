package com.seancoyle.spacex.framework.datasource.network.launch

import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.spacex.framework.datasource.api.launch.FakeLaunchApi
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeLaunchRetrofitServiceImpl
@Inject
constructor(
    private val api: FakeLaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchRetrofitService {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel> {
        return networkMapper.mapEntityToList(
            api.getLaunchList(options = launchOptions)
        )
    }

}












