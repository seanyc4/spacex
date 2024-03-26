package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.Crashlytics
import com.seancoyle.core_database.api.CompanyDao
import com.seancoyle.launch.api.domain.model.Company
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CompanyCacheDataSourceImpl @Inject constructor(
    private val dao: CompanyDao,
    private val entityMapper: CompanyEntityMapper,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : CompanyCacheDataSource {

    override suspend fun getCompany(): DataResult<Company?> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getCompanyInfo()?.let {
                entityMapper.mapFromEntity(it)
            }
        }
    }

    override suspend fun insert(company: Company): DataResult<Long> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.insert(entityMapper.mapToEntity(company))
        }
    }

    override suspend fun deleteAll(): DataResult<Unit> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteAll()
        }
    }
}