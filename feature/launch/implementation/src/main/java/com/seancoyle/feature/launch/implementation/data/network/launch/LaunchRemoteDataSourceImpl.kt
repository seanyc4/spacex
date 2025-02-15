package com.seancoyle.feature.launch.implementation.data.network.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchRemoteDataSource
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class LaunchRemoteDataSourceImpl @Inject constructor(
    private val api: LaunchApiService,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : LaunchRemoteDataSource {

    override suspend fun getLaunches(launchOptions: LaunchOptions): Result<LaunchesDto> {
        return withContext(ioDispatcher) {
            runCatching {
                api.getLaunches(launchOptions)
            }.fold(
                onSuccess = { Result.success(it) },
                onFailure = { exception ->
                    Timber.e(exception)
                    crashlytics.logException(exception)
                    Result.failure(exception)
                }
            )
        }
    }
}