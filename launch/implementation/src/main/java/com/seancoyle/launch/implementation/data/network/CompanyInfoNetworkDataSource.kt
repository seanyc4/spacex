package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.implementation.domain.model.Company

internal interface CompanyInfoNetworkDataSource {
    suspend fun getCompany(): Company
}