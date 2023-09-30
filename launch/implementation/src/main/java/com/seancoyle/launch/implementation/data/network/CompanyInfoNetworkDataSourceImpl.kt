package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfo
import javax.inject.Inject

class CompanyInfoNetworkDataSourceImpl @Inject constructor(
    private val api: CompanyInfoApi,
    private val networkMapper: CompanyInfoNetworkMapper
) : CompanyInfoNetworkDataSource {

    override suspend fun getCompanyInfo(): CompanyInfo {
        return networkMapper.mapFromEntity(
            api.getCompanyInfo()
        )
    }

}