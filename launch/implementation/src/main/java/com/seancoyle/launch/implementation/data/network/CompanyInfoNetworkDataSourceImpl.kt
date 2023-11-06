package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.domain.model.Company
import javax.inject.Inject

internal class CompanyInfoNetworkDataSourceImpl @Inject constructor(
    private val api: CompanyApi,
    private val networkMapper: CompanyInfoNetworkMapper
) : CompanyInfoNetworkDataSource {

    override suspend fun getCompany(): Company {
        return networkMapper.mapFromEntity(
            api.getCompany()
        )
    }

}