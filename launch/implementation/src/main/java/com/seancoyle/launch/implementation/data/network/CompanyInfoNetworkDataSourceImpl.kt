package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.api.model.CompanyInfoModel
import javax.inject.Inject

class CompanyInfoNetworkDataSourceImpl
@Inject
constructor(
    private val api: CompanyInfoApi,
    private val networkMapper: CompanyInfoNetworkMapper
) : com.seancoyle.launch.api.CompanyInfoNetworkDataSource {

    override suspend fun getCompanyInfo(): CompanyInfoModel {
        return networkMapper.mapFromEntity(
            api.getCompanyInfo()
        )
    }

}





























