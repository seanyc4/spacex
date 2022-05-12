package com.seancoyle.spacex.business.data.cache.abstraction.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity

interface CompanyInfoCacheDataSource {

    suspend fun insert(company: CompanyInfoDomainEntity): Long

    suspend fun getCompanyInfo(): CompanyInfoDomainEntity?

}






