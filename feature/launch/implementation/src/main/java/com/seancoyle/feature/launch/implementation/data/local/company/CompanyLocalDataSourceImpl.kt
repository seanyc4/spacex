package com.seancoyle.feature.launch.implementation.data.local.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.CompanyDao
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.mapper.LocalErrorMapper
import com.seancoyle.feature.launch.implementation.data.mapper.CompanyMapper
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyLocalDataSource
import timber.log.Timber
import javax.inject.Inject

internal class CompanyLocalDataSourceImpl @Inject constructor(
    private val dao: CompanyDao,
    private val crashlytics: Crashlytics,
    private val errorMapper: LocalErrorMapper,
    private val mapper: CompanyMapper
) : CompanyLocalDataSource {

    override suspend fun get(): LaunchResult<Company, LocalError> {
        return runCatching { dao.getCompany() }
            .fold(
                onSuccess = { result ->
                    result?.let {
                        LaunchResult.Success(mapper.entityToDomain(it))
                    } ?: LaunchResult.Error(LocalError.CACHE_ERROR_NO_RESULTS)
                },
                onFailure = { exception ->
                    Timber.e(exception)
                    crashlytics.logException(exception)
                    LaunchResult.Error(errorMapper.map(exception))
                }
            )
    }

    override suspend fun insert(company: Company): LaunchResult<Long, LocalError> {
        return runCatching { dao.insert(mapper.domainToEntity(company)) }
            .fold(
                onSuccess = { LaunchResult.Success(it) },
                onFailure = { exception ->
                    Timber.e(exception)
                    crashlytics.logException(exception)
                    LaunchResult.Error(errorMapper.map(exception))
                }
            )
    }

    override suspend fun deleteAll(): LaunchResult<Unit, LocalError> {
        return runCatching { dao.deleteAll() }
            .fold(
                onSuccess = { LaunchResult.Success(Unit) },
                onFailure = { exception ->
                    Timber.e(exception)
                    crashlytics.logException(exception)
                    LaunchResult.Error(errorMapper.map(exception))
                }
            )
    }

}
