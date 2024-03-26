package com.seancoyle.launch.implementation.domain.network

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Company

internal interface CompanyInfoNetworkDataSource {
    suspend fun getCompany(): DataResult<Company>
}