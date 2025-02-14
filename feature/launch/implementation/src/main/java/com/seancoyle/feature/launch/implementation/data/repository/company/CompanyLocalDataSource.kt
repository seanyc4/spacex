package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.entities.CompanyEntity

internal interface CompanyLocalDataSource {
    suspend fun insert(company: CompanyEntity): LaunchResult<Long, DataSourceError>
    suspend fun get(): LaunchResult<CompanyEntity?, DataSourceError>
    suspend fun deleteAll(): LaunchResult<Unit, DataSourceError>
}