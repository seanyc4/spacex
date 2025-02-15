package com.seancoyle.feature.launch.implementation.data.network.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class CompanyRemoteDataSourceImpl @Inject constructor(
    private val api: CompanyApiService,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : CompanyRemoteDataSource {

    override suspend fun getCompanyApi(): Result<CompanyDto> {
        return withContext(ioDispatcher) {
            runCatching {
                api.getCompany()
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