package com.seancoyle.feature.launch.implementation.data.cache.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.CompanyDao
import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class CompanyLocalDataSourceImpl @Inject constructor(
    private val dao: CompanyDao,
    private val crashlytics: Crashlytics,
    private val localDataSourceErrorMapper: LocalDataSourceErrorMapper,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : CompanyLocalDataSource {

    override suspend fun get(): LaunchResult<CompanyEntity?, LocalError> {
        return withContext(ioDispatcher) {
            runCatching { dao.getCompany() }
                .fold(
                    onSuccess = { LaunchResult.Success(it) },
                    onFailure = { exception ->
                        Timber.e(exception)
                        crashlytics.logException(exception)
                        LaunchResult.Error(localDataSourceErrorMapper.map(exception))
                    }
                )
        }
    }

    override suspend fun insert(company: CompanyEntity): LaunchResult<Long, LocalError> {
        return withContext(ioDispatcher) {
            runCatching { dao.insert(company) }
                .fold(
                    onSuccess = { LaunchResult.Success(it) },
                    onFailure = { exception ->
                        Timber.e(exception)
                        crashlytics.logException(exception)
                        LaunchResult.Error(localDataSourceErrorMapper.map(exception))
                    }
                )
        }
    }

    override suspend fun deleteAll(): LaunchResult<Unit, LocalError> {
        return withContext(ioDispatcher) {
            runCatching { dao.deleteAll() }
                .fold(
                    onSuccess = { LaunchResult.Success(Unit) },
                    onFailure = { exception ->
                        Timber.e(exception)
                        crashlytics.logException(exception)
                        LaunchResult.Error(localDataSourceErrorMapper.map(exception))
                    }
                )
        }
    }
}
