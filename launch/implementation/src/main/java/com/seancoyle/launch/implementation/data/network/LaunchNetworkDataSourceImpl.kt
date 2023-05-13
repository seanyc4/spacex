package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.launch.api.domain.model.ViewModel
import javax.inject.Inject

class LaunchNetworkDataSourceImpl
@Inject
constructor(
    private val api: LaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<ViewModel> {
        return networkMapper.mapEntityToList(
            api.getLaunchList(options = launchOptions)
        )
    }

}





























