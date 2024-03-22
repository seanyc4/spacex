package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.api.domain.model.Company

internal interface CompanyInfoNetworkDataSource {
    suspend fun getCompany(): Company
}