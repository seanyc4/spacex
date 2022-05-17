package com.seancoyle.spacex.business.data.cache.abstraction.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel

interface CompanyInfoCacheDataSource {

    suspend fun insert(company: CompanyInfoModel): Long

    suspend fun getCompanyInfo(): CompanyInfoModel?

    suspend fun deleteAll()

}






