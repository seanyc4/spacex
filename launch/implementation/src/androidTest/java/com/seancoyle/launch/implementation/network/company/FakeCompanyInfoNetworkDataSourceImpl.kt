package com.seancoyle.launch.implementation.network.company

import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkMapper
import com.seancoyle.launch.implementation.domain.network.CompanyInfoNetworkDataSource
import javax.inject.Inject

internal class FakeCompanyInfoNetworkDataSourceImpl @Inject constructor(
    private val fakeApi: FakeCompanyApi,
    private val networkMapper: CompanyInfoNetworkMapper
) : CompanyInfoNetworkDataSource {

    override suspend fun getCompany(): Company {
        return networkMapper.mapFromEntity(
            fakeApi.getCompany()
        )
    }
}