package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.core_database.api.CompanyDao
import com.seancoyle.launch.api.domain.model.Company
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CompanyCacheDataSourceImpl @Inject constructor(
    private val dao: CompanyDao,
    private val entityMapper: CompanyEntityMapper
) : CompanyCacheDataSource {

    override suspend fun getCompany(): Company? {
        return dao.getCompanyInfo()?.let {
            entityMapper.mapFromEntity(it)
        }
    }

    override suspend fun insert(company: Company): Long {
        return dao.insert(entityMapper.mapToEntity(company))
    }

    override suspend fun deleteAll() {
        return dao.deleteAll()
    }
}