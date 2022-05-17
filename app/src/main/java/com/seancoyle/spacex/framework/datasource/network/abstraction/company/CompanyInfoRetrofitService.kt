package com.seancoyle.spacex.framework.datasource.network.abstraction.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel

interface CompanyInfoRetrofitService {

    suspend fun getCompanyInfo(): CompanyInfoModel

}