package com.seancoyle.launch_datasource_test.network

import com.google.gson.GsonBuilder
import com.seancoyle.launch_datasource.network.CompanyInfoApi
import com.seancoyle.launch_datasource.network.CompanyInfoNetworkDataSource
import com.seancoyle.launch_datasource.network.CompanyInfoNetworkMapper
import com.seancoyle.launch_models.model.company.CompanyInfoModel
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


    override suspend fun getCompanyInfo(): CompanyInfoModel {
        return networkMapper.mapFromEntity(
            companyInfoApi.getCompanyInfo()
        )
    }

}
