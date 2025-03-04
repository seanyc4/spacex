package com.seancoyle.feature.launch.implementation.data.network.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.RemoteError.NETWORK_DATA_NULL
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.mapper.CompanyMapper
import com.seancoyle.feature.launch.implementation.data.mapper.RemoteErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyRemoteDataSource
import timber.log.Timber
import javax.inject.Inject

internal class CompanyRemoteDataSourceImpl @Inject constructor(
    private val api: CompanyApi,
    private val errorMapper: RemoteErrorMapper,
    private val crashlytics: Crashlytics,
    private val mapper: CompanyMapper
) : CompanyRemoteDataSource {

    override suspend fun getCompanyApi(): LaunchResult<Company, DataError> {
        return runCatching {
            api.getCompany()
        }.fold(
            onSuccess = { result ->
                result?.let { LaunchResult.Success(mapper.dtoToDomain(it)) }
                    ?: LaunchResult.Error(NETWORK_DATA_NULL)
            },
            onFailure = { exception ->
                Timber.e(exception)
                crashlytics.logException(exception)
                LaunchResult.Error(errorMapper.map(exception))
            }
        )
    }
}