package com.seancoyle.spacex.business.data.network.implementation.company

import com.seancoyle.spacex.business.data.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.framework.datasource.network.abstraction.company.CompanyInfoRetrofitService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CompanyInfoNetworkDataSourceImpl
@Inject
constructor(
    private val retrofitService: CompanyInfoRetrofitService
) : CompanyInfoNetworkDataSource {

    override suspend fun getCompanyInfo(): CompanyInfoModel {
        return retrofitService.getCompanyInfo()
    }

}





























