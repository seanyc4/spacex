package com.seancoyle.feature.launch.implementation.data.cache.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
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
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : CompanyLocalDataSource {

    override suspend fun get(): Result<CompanyEntity?> {
        return withContext(ioDispatcher) {
            runCatching { dao.getCompany() }
                .fold(
                    onSuccess = { Result.success(it) },
                    onFailure = { exception ->
                        Timber.e(exception)
                        crashlytics.logException(exception)
                        Result.failure(exception)
                    }
                )
        }
    }

    override suspend fun insert(company: CompanyEntity): Result<Long> {
        return withContext(ioDispatcher) {
            runCatching { dao.insert(company) }
                .fold(
                    onSuccess = { Result.success(it) },
                    onFailure = { exception ->
                        Timber.e(exception)
                        crashlytics.logException(exception)
                        Result.failure(exception)
                    }
                )
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return withContext(ioDispatcher) {
            runCatching { dao.deleteAll() }
                .fold(
                    onSuccess = { Result.success(Unit) },
                    onFailure = { exception ->
                        Timber.e(exception)
                        crashlytics.logException(exception)
                        Result.failure(exception)
                    }
                )
        }
    }
}
