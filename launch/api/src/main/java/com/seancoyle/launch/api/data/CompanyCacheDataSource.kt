package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.Company

interface CompanyCacheDataSource {
    suspend fun insert(company: Company): Long
    suspend fun getCompany(): Company?
    suspend fun deleteAll()
}