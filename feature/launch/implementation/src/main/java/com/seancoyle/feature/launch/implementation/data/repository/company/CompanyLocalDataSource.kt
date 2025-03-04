package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company

internal interface CompanyLocalDataSource {
    suspend fun insert(company: Company): LaunchResult<Long, LocalError>
    suspend fun get(): LaunchResult<Company, LocalError>
    suspend fun deleteAll(): LaunchResult<Unit, LocalError>
}