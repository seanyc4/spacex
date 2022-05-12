package com.seancoyle.spacex.framework.datasource.network.implementation.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.framework.datasource.network.abstraction.company.CompanyInfoRetrofitService
import com.seancoyle.spacex.framework.datasource.network.api.company.CompanyInfoService
import com.seancoyle.spacex.framework.datasource.network.mappers.company.CompanyInfoNetworkMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyInfoRetrofitServiceImpl
@Inject
constructor(
    private val service: CompanyInfoService,
    private val networkMapper: CompanyInfoNetworkMapper
) : CompanyInfoRetrofitService {

    override suspend fun getCompanyInfo(): CompanyInfoDomainEntity {
        return networkMapper.mapFromEntity(
            service.getCompanyInfo()
        )
    }
}












