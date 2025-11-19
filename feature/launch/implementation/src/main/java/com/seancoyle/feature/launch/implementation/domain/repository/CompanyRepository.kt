package com.seancoyle.feature.launch.implementation.domain.repository

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company

interface CompanyRepository {
    suspend fun getCompanyApi(): LaunchResult<Unit, DataError>
    suspend fun getCompanyCache(): LaunchResult<Company?, LocalError>
    suspend fun deleteAllCompanyCache(): LaunchResult<Unit, LocalError>
}
