package com.seancoyle.launch_datasource.cache

import com.seancoyle.database.daos.CompanyInfoDao
import com.seancoyle.launch_models.model.company.CompanyInfoModel
import javax.inject.Inject

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





















