package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.contract.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.contract.domain.model.CompanyInfo
import javax.inject.Inject

class CompanyInfoNetworkDataSourceImpl
@Inject
constructor(
    private val api: CompanyInfoApi,
    private val networkMapper: CompanyInfoNetworkMapper
) : CompanyInfoNetworkDataSource {

    override suspend fun getCompanyInfo(): CompanyInfo {
        return networkMapper.mapFromEntity(
            api.getCompanyInfo()
        )
    }

}





























