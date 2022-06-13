package com.seancoyle.launch_datasource.network.abstraction.company

import com.seancoyle.launch_domain.model.company.CompanyInfoModel

interface CompanyInfoNetworkDataSource {

    suspend fun getCompanyInfo(): CompanyInfoModel

}
