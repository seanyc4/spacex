package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.Company

interface CompanyInfoNetworkDataSource {
    suspend fun getCompany(): Company
}