package com.seancoyle.feature.launch.implementation.domain.network

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company

internal interface CompanyInfoNetworkDataSource {
    suspend fun getCompany(): Result<Company, DataError>
}