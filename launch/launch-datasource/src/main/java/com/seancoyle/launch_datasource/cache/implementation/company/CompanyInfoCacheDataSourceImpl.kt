package com.seancoyle.launch_datasource.cache.implementation.company

import com.seancoyle.launch_datasource.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.launch_datasource.cache.mappers.company.CompanyInfoEntityMapper
import com.seancoyle.launch_domain.model.company.CompanyInfoModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyInfoCacheDataSourceImpl
@Inject
constructor(
    private val dao: CompanyInfoDao,
    private val entityMapper: CompanyInfoEntityMapper
) : CompanyInfoCacheDataSource {

    override suspend fun insert(company: CompanyInfoModel): Long {
        return dao.insert(
            entityMapper.mapToEntity(
                domainModel = company
            )
        )
    }

    override suspend fun getCompanyInfo(): CompanyInfoModel? {
        return dao.getCompanyInfo()?.let {
            entityMapper.mapFromEntity(it)
        }
    }

    override suspend fun deleteAll() {
        return dao.deleteAll()
    }
}





















