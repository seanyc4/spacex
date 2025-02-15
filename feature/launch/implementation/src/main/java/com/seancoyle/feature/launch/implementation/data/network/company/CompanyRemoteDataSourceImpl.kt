package com.seancoyle.feature.launch.implementation.data.network.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.data.cache.launch.RemoteDataSourceErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class CompanyRemoteDataSourceImpl @Inject constructor(
    private val api: CompanyApiService,
    private val remoteDataSourceErrorMapper: RemoteDataSourceErrorMapper,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : CompanyRemoteDataSource {

    override suspend fun getCompanyApi(): LaunchResult<CompanyDto, RemoteError> {
        return withContext(ioDispatcher) {
            runCatching {
                api.getCompany()
            }.fold(
                onSuccess = { LaunchResult.Success(it) },
                onFailure = { exception ->
                    Timber.e(exception)
                    crashlytics.logException(exception)
                    LaunchResult.Error(remoteDataSourceErrorMapper.map(exception))
                }
            )
        }
    }
}