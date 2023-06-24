package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.database.daos.CompanyInfoDao
import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfo
import javax.inject.Inject

class CompanyInfoCacheDataSourceImpl
@Inject
constructor(
    private val dao: CompanyInfoDao,
    private val entityMapper: CompanyInfoEntityMapper
) : CompanyInfoCacheDataSource {

    override suspend fun insert(company: CompanyInfo): Long {
        return dao.insert(
            entityMapper.mapToEntity(
                domainModel = company
            )
        )
    }

    override suspend fun getCompanyInfo(): CompanyInfo? {
        return dao.getCompanyInfo()?.let {
            entityMapper.mapFromEntity(it)
        }
    }

    override suspend fun deleteAll() {
        return dao.deleteAll()
    }
}





















