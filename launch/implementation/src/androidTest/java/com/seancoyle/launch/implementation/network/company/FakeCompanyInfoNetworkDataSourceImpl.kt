package com.seancoyle.launch.implementation.network.company

import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkDataSource
import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkMapper
import com.seancoyle.launch.implementation.domain.model.Company
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