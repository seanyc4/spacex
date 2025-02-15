package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDto

internal interface CompanyRemoteDataSource {
    suspend fun getCompanyApi(): Result<CompanyDto>
}