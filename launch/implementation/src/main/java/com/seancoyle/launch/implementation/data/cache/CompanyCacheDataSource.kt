package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.launch.implementation.domain.model.Company

internal interface CompanyCacheDataSource {
    suspend fun insert(company: Company): Long
    suspend fun getCompany(): Company?
    suspend fun deleteAll()
}