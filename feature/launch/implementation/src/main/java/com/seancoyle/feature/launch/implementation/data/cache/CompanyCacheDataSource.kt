package com.seancoyle.feature.launch.implementation.data.cache

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.database.entities.CompanyEntity

internal interface CompanyCacheDataSource {
    suspend fun insert(company: CompanyEntity): Result<Long, DataError>
    suspend fun getCompany(): Result<CompanyEntity?, DataError>
    suspend fun deleteAll(): Result<Unit, DataError>
}