package com.seancoyle.spacex.framework.datasource.network.launch

import com.seancoyle.launch.contract.data.LaunchNetworkDataSource
import com.seancoyle.launch.contract.domain.model.LaunchOptions
import com.seancoyle.launch.contract.domain.model.ViewModel
import com.seancoyle.launch.implementation.data.network.LaunchNetworkMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeLaunchNetworkDataSourceImpl @Inject constructor(
    private val fakeApi: FakeLaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<ViewModel> {
        return networkMapper.mapEntityToList(
            fakeApi.getLaunchList(options = launchOptions)
        )
    }
}