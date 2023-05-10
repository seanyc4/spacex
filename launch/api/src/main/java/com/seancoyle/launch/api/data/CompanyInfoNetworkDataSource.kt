package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.CompanyInfoModel

interface CompanyInfoNetworkDataSource {

    suspend fun getCompanyInfo(): CompanyInfoModel

}
