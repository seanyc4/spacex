package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LaunchNetworkDataSourceImpl @Inject constructor(
    private val api: LaunchApi,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    override suspend fun getLaunches(launchOptions: LaunchOptions): List<Launch> {
        return networkMapper.mapEntityToList(
            api.getLaunches(launchOptions)
        )
    }

}