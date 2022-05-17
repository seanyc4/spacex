package com.seancoyle.spacex.business.data.cache.implementation.company

import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.framework.datasource.cache.abstraction.company.CompanyInfoDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyInfoCacheDataSourceImpl
@Inject
constructor(
    private val daoService: CompanyInfoDaoService
) : CompanyInfoCacheDataSource {

    override suspend fun insert(company: CompanyInfoModel): Long {
        return daoService.insert(
            company = company
        )
    }

    override suspend fun getCompanyInfo(): CompanyInfoModel? {
        return daoService.getCompanyInfo()
    }

    override suspend fun deleteAll() {
        return daoService.deleteAll()
    }
}





















