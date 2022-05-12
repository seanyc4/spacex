package com.seancoyle.spacex.framework.datasource.cache.implementation.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.framework.datasource.cache.abstraction.company.CompanyInfoDaoService
import com.seancoyle.spacex.framework.datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.spacex.framework.datasource.cache.mappers.company.CompanyInfoCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyInfoDaoServiceImpl
@Inject
constructor(
    private val dao: CompanyInfoDao,
    private val cacheMapper: CompanyInfoCacheMapper
) : CompanyInfoDaoService {

    override suspend fun insert(company: CompanyInfoDomainEntity): Long {
        return dao.insert(
            cacheMapper.mapToEntity(
                domainModel = company
            )
        )
    }

    override suspend fun getCompanyInfo(): CompanyInfoDomainEntity? {
        return dao.getCompanyInfo()?.let {
            cacheMapper.mapFromEntity(it)
        }
    }

}













