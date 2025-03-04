package com.seancoyle.feature.launch.implementation.data.network.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.RemoteError.NETWORK_DATA_NULL
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.data.cache.launch.RemoteErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyRemoteDataSource
import timber.log.Timber
import javax.inject.Inject

internal class CompanyRemoteDataSourceImpl @Inject constructor(
    private val api: CompanyApi,
    private val remoteErrorMapper: RemoteErrorMapper,
    private val crashlytics: Crashlytics,
) : CompanyRemoteDataSource {

    override suspend fun getCompanyApi(): LaunchResult<CompanyDto, DataError> {
        return runCatching {
            api.getCompany()
        }.fold(
            onSuccess = { result ->
                result?.let { LaunchResult.Success(it) }
                    ?: LaunchResult.Error(NETWORK_DATA_NULL)
            },
            onFailure = { exception ->
                Timber.e(exception)
                crashlytics.logException(exception)
                LaunchResult.Error(remoteErrorMapper.map(exception))
            }
        )
    }
}