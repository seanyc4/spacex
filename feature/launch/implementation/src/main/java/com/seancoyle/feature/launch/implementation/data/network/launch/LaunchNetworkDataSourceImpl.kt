package com.seancoyle.feature.launch.implementation.data.network.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.data.safeApiCall
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchNetworkDataSource
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class LaunchNetworkDataSourceImpl @Inject constructor(
    private val api: LaunchApiService,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : LaunchNetworkDataSource {

    override suspend fun getLaunches(launchOptions: LaunchOptions): LaunchResult<LaunchesDto, DataSourceError> {
        return safeApiCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            api.getLaunches(launchOptions)
        }
    }
}