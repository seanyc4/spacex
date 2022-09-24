package com.seancoyle.launch_datasource.network.abstraction.company

import com.seancoyle.launch_models.model.company.CompanyInfoModel

interface CompanyInfoNetworkDataSource {

    suspend fun getCompanyInfo(): CompanyInfoModel

}
