package com.seancoyle.feature.launch.implementation.data.network.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.data.cache.launch.RemoteDataSourceErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchRemoteDataSource
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class LaunchRemoteDataSourceImpl @Inject constructor(
    private val api: LaunchApiService,
    private val remoteDataSourceErrorMapper: RemoteDataSourceErrorMapper,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : LaunchRemoteDataSource {

    override suspend fun getLaunches(launchOptions: LaunchOptions): LaunchResult<LaunchesDto, RemoteError> {
        return withContext(ioDispatcher) {
            runCatching {
                api.getLaunches(launchOptions)
            }.fold(
                onSuccess = { result ->
                    result?.let { LaunchResult.Success(it) }
                        ?: LaunchResult.Error(RemoteError.NETWORK_DATA_NULL)
                },
                onFailure = { exception ->
                    Timber.e(exception)
                    crashlytics.logException(exception)
                    LaunchResult.Error(remoteDataSourceErrorMapper.map(exception))
                }
            )
        }
    }
}
