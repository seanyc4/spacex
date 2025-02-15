package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDto

internal interface CompanyRemoteDataSource {
    suspend fun getCompanyApi(): LaunchResult<CompanyDto, RemoteError>
}