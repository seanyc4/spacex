package com.seancoyle.launch.contract.data

import com.seancoyle.launch.contract.domain.model.CompanyInfo

interface CompanyInfoCacheDataSource {

    suspend fun insert(company: CompanyInfo): Long

    suspend fun getCompanyInfo(): CompanyInfo?

    suspend fun deleteAll()

}






