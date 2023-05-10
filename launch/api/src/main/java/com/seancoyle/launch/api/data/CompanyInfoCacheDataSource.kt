package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.CompanyInfoModel

interface CompanyInfoCacheDataSource {

    suspend fun insert(company: CompanyInfoModel): Long

    suspend fun getCompanyInfo(): CompanyInfoModel?

    suspend fun deleteAll()

}






