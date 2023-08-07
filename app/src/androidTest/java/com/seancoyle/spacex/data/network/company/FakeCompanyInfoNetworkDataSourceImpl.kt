package com.seancoyle.spacex.data.network.company

import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfo
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