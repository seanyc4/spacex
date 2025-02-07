package com.seancoyle.feature.launch.implementation.data.cache

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.database.dao.CompanyDao
import com.seancoyle.database.entities.CompanyEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class CompanyCacheDataSourceImpl @Inject constructor(
    private val dao: CompanyDao,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : CompanyCacheDataSource {

    override suspend fun getCompany(): Result<CompanyEntity?, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getCompanyInfo()
        }
    }

    override suspend fun insert(company: CompanyEntity): Result<Long, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.insert(company)
        }
    }

    override suspend fun deleteAll(): Result<Unit, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteAll()
        }
    }
}