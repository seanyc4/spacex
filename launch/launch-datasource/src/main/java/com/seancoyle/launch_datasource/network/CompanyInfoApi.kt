package com.seancoyle.launch_datasource.network

import retrofit2.http.GET

interface CompanyInfoApi {

    @GET("/v3/info")
    suspend fun getCompanyInfo(): CompanyInfoDto

}