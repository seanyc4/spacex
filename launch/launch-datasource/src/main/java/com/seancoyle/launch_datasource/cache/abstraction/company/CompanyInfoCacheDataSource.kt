package com.seancoyle.launch_datasource.cache.abstraction.company

import com.seancoyle.launch_domain.model.company.CompanyInfoModel

interface CompanyInfoCacheDataSource {

    suspend fun insert(company: CompanyInfoModel): Long

    suspend fun getCompanyInfo(): CompanyInfoModel?

    suspend fun deleteAll()

}






