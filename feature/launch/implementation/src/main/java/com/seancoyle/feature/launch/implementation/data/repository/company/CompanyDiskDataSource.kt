package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.database.entities.CompanyEntity

internal interface CompanyDiskDataSource {
    suspend fun insert(company: CompanyEntity): Result<Long, DataError>
    suspend fun get(): Result<CompanyEntity?, DataError>
    suspend fun deleteAll(): Result<Unit, DataError>
}