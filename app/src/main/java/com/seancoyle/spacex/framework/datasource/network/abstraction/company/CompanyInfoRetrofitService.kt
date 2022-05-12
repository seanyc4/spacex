package com.seancoyle.spacex.framework.datasource.network.abstraction.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity

interface CompanyInfoRetrofitService {

    suspend fun getCompanyInfo(): CompanyInfoDomainEntity

}