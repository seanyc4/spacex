package com.seancoyle.spacex.framework.datasource.cache.abstraction.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity

interface CompanyInfoDaoService {

    suspend fun insert(company: CompanyInfoDomainEntity): Long

    suspend fun getCompanyInfo(): CompanyInfoDomainEntity?

}






