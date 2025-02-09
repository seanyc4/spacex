package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDto

internal interface CompanyNetworkDataSource {
    suspend fun getCompanyApi(): Result<CompanyDto, DataError>
}