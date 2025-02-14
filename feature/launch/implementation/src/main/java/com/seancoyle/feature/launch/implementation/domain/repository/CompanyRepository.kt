package com.seancoyle.feature.launch.implementation.domain.repository

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company

interface CompanyRepository {
    suspend fun getCompanyApi(): LaunchResult<Unit, DataSourceError>
    suspend fun getCompanyCache(): LaunchResult<Company?, DataSourceError>
    suspend fun deleteAllCompanyCache(): LaunchResult<Unit, DataSourceError>
}