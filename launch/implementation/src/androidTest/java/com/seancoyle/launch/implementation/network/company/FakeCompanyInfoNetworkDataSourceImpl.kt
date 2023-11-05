package com.seancoyle.launch.implementation.network.company

import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkMapper
import javax.inject.Inject

internal class FakeCompanyInfoNetworkDataSourceImpl @Inject constructor(
    private val fakeApi: FakeCompanyApi,
    private val networkMapper: CompanyInfoNetworkMapper
) : CompanyInfoNetworkDataSource {

    override suspend fun getCompanyInfo(): Company {
        return networkMapper.mapFromEntity(
            fakeApi.getCompanyInfo()
        )
    }
}