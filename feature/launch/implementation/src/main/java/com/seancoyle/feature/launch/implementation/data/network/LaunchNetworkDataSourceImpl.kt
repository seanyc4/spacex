package com.seancoyle.feature.launch.implementation.data.network

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.data.safeApiCall
import com.seancoyle.feature.launch.implementation.data.network.dto.LaunchesDto
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.network.LaunchNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class LaunchNetworkDataSourceImpl @Inject constructor(
    private val api: LaunchApiService,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : LaunchNetworkDataSource {

    override suspend fun getLaunches(launchOptions: LaunchOptions): Result<LaunchesDto, DataError> {
        return safeApiCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            api.getLaunches(launchOptions)
        }
    }
}