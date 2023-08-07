package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.CompanyInfo

interface CompanyInfoNetworkDataSource {

    suspend fun getCompanyInfo(): CompanyInfo

}
