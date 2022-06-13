package com.seancoyle.launch_datasource.network.implementation.launch

import com.seancoyle.launch_datasource.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.launch_datasource.network.api.launch.LaunchApi
import com.seancoyle.launch_datasource.network.mappers.launch.LaunchNetworkMapper
import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.launch_domain.model.launch.LaunchOptions
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LaunchNetworkDataSourceImpl
@Inject
constructor(
    private val api: LaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel> {
        return networkMapper.mapEntityToList(
            api.getLaunchList(options = launchOptions)
        )
    }

}





























