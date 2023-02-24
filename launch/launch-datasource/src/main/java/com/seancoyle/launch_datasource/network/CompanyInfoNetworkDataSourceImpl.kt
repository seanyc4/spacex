package com.seancoyle.launch_datasource.network

import com.seancoyle.launch_models.model.company.CompanyInfoModel
import javax.inject.Inject

class CompanyInfoNetworkDataSourceImpl
@Inject
constructor(
    private val api: CompanyInfoApi,
    private val networkMapper: CompanyInfoNetworkMapper
) : CompanyInfoNetworkDataSource {

    override suspend fun getCompanyInfo(): CompanyInfoModel {
        return networkMapper.mapFromEntity(
            api.getCompanyInfo()
        )
    }

}





























