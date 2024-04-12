package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.domain.Crashlytics
import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.core.domain.di.IODispatcher
import com.seancoyle.database.dao.CompanyDao
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.domain.cache.CompanyCacheDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CompanyCacheDataSourceImpl @Inject constructor(
    private val dao: CompanyDao,
    private val mapper: CompanyEntityMapper,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : CompanyCacheDataSource {

    override suspend fun getCompany(): DataResult<Company?, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getCompanyInfo()?.let {
                mapper.mapFromEntity(it)
            }
        }
    }

    override suspend fun insert(company: Company): DataResult<Long, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.insert(mapper.mapToEntity(company))
        }
    }

    override suspend fun deleteAll(): DataResult<Unit, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteAll()
        }
    }
}