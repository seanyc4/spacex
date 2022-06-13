package com.seancoyle.launch_datasource.network.implementation.company

import com.seancoyle.launch_datasource.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.launch_datasource.network.api.company.CompanyInfoApi
import com.seancoyle.launch_datasource.network.mappers.company.CompanyInfoNetworkMapper
import com.seancoyle.launch_domain.model.company.CompanyInfoModel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
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





























