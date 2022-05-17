package com.seancoyle.spacex.framework.datasource.cache.implementation.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.framework.datasource.cache.abstraction.company.CompanyInfoDaoService
import com.seancoyle.spacex.framework.datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.spacex.framework.datasource.cache.mappers.company.CompanyInfoEntityMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyInfoDaoServiceImpl
@Inject
constructor(
    private val dao: CompanyInfoDao,
    private val entityMapper: CompanyInfoEntityMapper
) : CompanyInfoDaoService {

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

}













