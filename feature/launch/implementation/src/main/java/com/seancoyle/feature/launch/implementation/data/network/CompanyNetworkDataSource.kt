package com.seancoyle.feature.launch.implementation.data.network

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.data.network.dto.CompanyDto

internal interface CompanyNetworkDataSource {
    suspend fun getCompany(): Result<CompanyDto, DataError>
}