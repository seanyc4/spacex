package com.seancoyle.launch.api

import com.seancoyle.launch.api.model.CompanyInfoModel

interface CompanyInfoNetworkDataSource {

    suspend fun getCompanyInfo(): CompanyInfoModel

}
