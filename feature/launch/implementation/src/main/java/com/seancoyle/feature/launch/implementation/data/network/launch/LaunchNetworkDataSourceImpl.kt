package com.seancoyle.feature.launch.implementation.data.network.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.data.cache.launch.RemoteDataSourceErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchNetworkDataSource
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class LaunchNetworkDataSourceImpl @Inject constructor(
    private val api: LaunchApiService,
    private val dataSourceErrorMapper: RemoteDataSourceErrorMapper,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : LaunchNetworkDataSource {

    override suspend fun getLaunches(launchOptions: LaunchOptions): LaunchResult<LaunchesDto, DataSourceError> {
        return withContext(ioDispatcher) {
            runCatching {
                api.getLaunches(launchOptions)
            }.fold(
                onSuccess = { result ->
                    result?.let { LaunchResult.Success(it) }
                        ?: LaunchResult.Error(DataSourceError.NETWORK_DATA_NULL)
                },
                onFailure = { exception ->
                    Timber.e(exception)
                    crashlytics.logException(exception)
                    LaunchResult.Error(dataSourceErrorMapper.map(exception))
                }
            )
        }
    }
}