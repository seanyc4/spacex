package com.seancoyle.feature.launch.implementation.data.network.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.mapper.LaunchMapper
import com.seancoyle.feature.launch.implementation.data.mapper.RemoteErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchRemoteDataSource
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import timber.log.Timber
import javax.inject.Inject

internal class LaunchRemoteDataSourceImpl @Inject constructor(
    private val api: LaunchApi,
    private val crashlytics: Crashlytics,
    private val errorMapper: RemoteErrorMapper,
    private val mapper: LaunchMapper
) : LaunchRemoteDataSource {

    override suspend fun getLaunches(launchOptions: LaunchOptions): LaunchResult<List<LaunchTypes.Launch>, RemoteError> {
        return runCatching {
            api.getLaunches(launchOptions)
        }.fold(
            onSuccess = { result ->
                result?.let { LaunchResult.Success(mapper.dtoToDomainList(it)) }
                    ?: LaunchResult.Error(RemoteError.NETWORK_DATA_NULL)
            },
            onFailure = { exception ->
                Timber.e(exception)
                crashlytics.logException(exception)
                LaunchResult.Error(errorMapper.map(exception))
            }
        )
    }
}