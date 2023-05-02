package com.seancoyle.spacex.framework.datasource.network.company

import com.seancoyle.launch.api.CompanyInfoModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCompanyInfoNetworkDataSourceImpl
@Inject
constructor(
    private val fakeApi: FakeCompanyInfoApi,
    private val networkMapper: com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkMapper
) : com.seancoyle.launch.api.CompanyInfoNetworkDataSource {

    override suspend fun getCompanyInfo(): com.seancoyle.launch.api.CompanyInfoModel {
        return networkMapper.mapFromEntity(
            fakeApi.getCompanyInfo()
        )
    }
}
