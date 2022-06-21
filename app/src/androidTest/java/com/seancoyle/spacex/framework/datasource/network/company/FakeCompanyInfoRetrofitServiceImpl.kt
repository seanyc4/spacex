package com.seancoyle.spacex.framework.datasource.network.company

import com.seancoyle.launch_models.model.company.CompanyInfoModel
import com.seancoyle.spacex.framework.datasource.api.company.FakeCompanyInfoApi
import com.seancoyle.spacex.framework.datasource.network.abstraction.company.CompanyInfoRetrofitService
import com.seancoyle.launch_datasource.network.mappers.company.CompanyInfoNetworkMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCompanyInfoRetrofitServiceImpl
@Inject
constructor(
    private val fakeApi: FakeCompanyInfoApi,
    private val networkMapper: CompanyInfoNetworkMapper
) : CompanyInfoRetrofitService {

    override suspend fun getCompanyInfo(): CompanyInfoModel {
        return networkMapper.mapFromEntity(
            fakeApi.getCompanyInfo()
        )
    }
}












