package com.seancoyle.spacex.framework.datasource.network.company

import com.seancoyle.launch.contract.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.contract.domain.model.CompanyInfo
import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCompanyInfoNetworkDataSourceImpl @Inject constructor(
    private val fakeApi: FakeCompanyInfoApi,
    private val networkMapper: CompanyInfoNetworkMapper
) : CompanyInfoNetworkDataSource {

    override suspend fun getCompanyInfo(): CompanyInfo {
        return networkMapper.mapFromEntity(
            fakeApi.getCompanyInfo()
        )
    }
}
