package com.seancoyle.launch.implementation.domain.network

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.launch.api.domain.model.Company

internal interface CompanyInfoNetworkDataSource {
    suspend fun getCompany(): DataResult<Company, DataError>
}