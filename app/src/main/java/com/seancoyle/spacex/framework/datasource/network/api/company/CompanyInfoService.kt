package com.seancoyle.spacex.framework.datasource.network.api.company

import com.seancoyle.spacex.framework.datasource.network.model.company.CompanyInfoNetworkEntity
import retrofit2.http.GET

interface CompanyInfoService {

    @GET("/v3/info")
    suspend fun getCompanyInfo(): CompanyInfoNetworkEntity

}