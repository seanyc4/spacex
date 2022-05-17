package com.seancoyle.spacex.framework.datasource.cache.abstraction.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel

interface CompanyInfoDaoService {

    suspend fun insert(company: CompanyInfoModel): Long

    suspend fun getCompanyInfo(): CompanyInfoModel?

}






