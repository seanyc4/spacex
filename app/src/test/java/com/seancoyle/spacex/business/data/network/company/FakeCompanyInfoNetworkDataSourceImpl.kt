package com.seancoyle.spacex.business.data.network.company

import com.google.gson.GsonBuilder
import com.seancoyle.spacex.business.data.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.framework.datasource.network.api.company.CompanyInfoService
import com.seancoyle.spacex.framework.datasource.network.mappers.company.CompanyInfoNetworkMapper
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FakeCompanyInfoNetworkDataSourceImpl
constructor(
    private val baseUrl: HttpUrl,
    private val networkMapper: CompanyInfoNetworkMapper
) : CompanyInfoNetworkDataSource {

    private val service: CompanyInfoService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(CompanyInfoService::class.java)


    override suspend fun getCompanyInfo(): CompanyInfoDomainEntity {
        return networkMapper.mapFromEntity(
            service.getCompanyInfo()
        )
    }

}
