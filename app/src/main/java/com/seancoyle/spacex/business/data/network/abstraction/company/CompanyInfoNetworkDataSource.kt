package com.seancoyle.spacex.business.data.network.abstraction.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel

interface CompanyInfoNetworkDataSource {

    suspend fun getCompanyInfo(): CompanyInfoModel

}
