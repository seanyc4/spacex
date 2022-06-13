package com.seancoyle.spacex.business.data.network.implementation.launch

import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LaunchNetworkDataSourceImpl
@Inject
constructor(
    private val retrofitService: LaunchRetrofitService
) : LaunchNetworkDataSource {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel> {
        return retrofitService.getLaunchList(launchOptions = launchOptions)
    }

}





























