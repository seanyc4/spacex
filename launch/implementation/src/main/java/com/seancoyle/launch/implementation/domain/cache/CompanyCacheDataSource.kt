package com.seancoyle.launch.implementation.domain.cache

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Company

internal interface CompanyCacheDataSource {
    suspend fun insert(company: Company): DataResult<Long>
    suspend fun getCompany(): DataResult<Company?>
    suspend fun deleteAll(): DataResult<Unit>
}