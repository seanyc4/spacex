package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.launch.api.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLaunchesFromNetworkAndInsertToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource,
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val launchOptions: LaunchOptions
) : GetLaunchesFromNetworkAndInsertToCacheUseCase {
    override operator fun invoke(): Flow<List<Launch>>  = flow {
        val result = launchNetworkDataSource.getLaunchList(launchOptions = launchOptions)

        if (result.isNotEmpty()) {
            cacheDataSource.insertList(result)
        }
    }

}