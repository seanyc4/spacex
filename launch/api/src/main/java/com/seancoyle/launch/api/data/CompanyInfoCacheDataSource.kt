package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.CompanyInfo

interface CompanyInfoCacheDataSource {

    suspend fun insert(company: CompanyInfo): Long

    suspend fun getCompanyInfo(): CompanyInfo?

    suspend fun deleteAll()

}






