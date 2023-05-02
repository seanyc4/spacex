package com.seancoyle.launch.api

import com.seancoyle.launch.api.model.CompanyInfoModel

interface CompanyInfoCacheDataSource {

    suspend fun insert(company: CompanyInfoModel): Long

    suspend fun getCompanyInfo(): CompanyInfoModel?

    suspend fun deleteAll()

}






