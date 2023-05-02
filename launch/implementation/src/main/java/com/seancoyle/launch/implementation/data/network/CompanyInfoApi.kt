package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.implementation.data.model.CompanyInfoDto
import retrofit2.http.GET

interface CompanyInfoApi {

    @GET("/v3/info")
    suspend fun getCompanyInfo(): CompanyInfoDto

}