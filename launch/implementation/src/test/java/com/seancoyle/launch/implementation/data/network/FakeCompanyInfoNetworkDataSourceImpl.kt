package com.seancoyle.launch.implementation.data.network

import com.google.gson.GsonBuilder
import com.seancoyle.launch.contract.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.contract.domain.model.CompanyInfo
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FakeCompanyInfoNetworkDataSourceImpl
constructor(
    private val baseUrl: HttpUrl,
    private val networkMapper: CompanyInfoNetworkMapper
) : CompanyInfoNetworkDataSource {

    private val companyInfoApi: CompanyInfoApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(CompanyInfoApi::class.java)


    override suspend fun getCompanyInfo(): CompanyInfo {
        return networkMapper.mapFromEntity(
            companyInfoApi.getCompanyInfo()
        )
    }

}
