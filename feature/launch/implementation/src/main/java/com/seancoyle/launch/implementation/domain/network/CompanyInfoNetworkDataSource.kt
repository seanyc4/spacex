package com.seancoyle.launch.implementation.domain.network

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
import com.seancoyle.launch.api.domain.model.Company

internal interface CompanyInfoNetworkDataSource {
    suspend fun getCompany(): DataResult<Company, DataError>
}