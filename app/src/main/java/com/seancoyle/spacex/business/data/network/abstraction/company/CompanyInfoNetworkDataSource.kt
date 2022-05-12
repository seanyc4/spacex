package com.seancoyle.spacex.business.data.network.abstraction.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity

interface CompanyInfoNetworkDataSource {

    suspend fun getCompanyInfo(): CompanyInfoDomainEntity

}
