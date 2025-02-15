package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.entities.CompanyEntity

internal interface CompanyLocalDataSource {
    suspend fun insert(company: CompanyEntity): LaunchResult<Long, LocalError>
    suspend fun get(): LaunchResult<CompanyEntity?, LocalError>
    suspend fun deleteAll(): LaunchResult<Unit, LocalError>
}