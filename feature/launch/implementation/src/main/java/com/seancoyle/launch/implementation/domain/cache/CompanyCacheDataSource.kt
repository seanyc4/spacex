package com.seancoyle.launch.implementation.domain.cache

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
import com.seancoyle.launch.api.domain.model.Company

internal interface CompanyCacheDataSource {
    suspend fun insert(company: Company): DataResult<Long, DataError>
    suspend fun getCompany(): DataResult<Company?, DataError>
    suspend fun deleteAll(): DataResult<Unit, DataError>
}