package com.seancoyle.feature.launch.implementation.domain.cache

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company

internal interface CompanyCacheDataSource {
    suspend fun insert(company: Company): Result<Long, DataError>
    suspend fun getCompany(): Result<Company?, DataError>
    suspend fun deleteAll(): Result<Unit, DataError>
}