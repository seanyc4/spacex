package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.CompanyInfo
import kotlinx.coroutines.flow.Flow

interface CompanyInfoCacheDataSource {

    suspend fun insert(company: CompanyInfo): Long

    fun getCompanyInfo(): Flow<CompanyInfo?>

    suspend fun deleteAll()

}