package com.seancoyle.launch.contract.data

import com.seancoyle.launch.contract.domain.model.CompanyInfo

interface CompanyInfoNetworkDataSource {

    suspend fun getCompanyInfo(): CompanyInfo

}
