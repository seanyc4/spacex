package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.implementation.data.model.CompanyInfoDto
import retrofit2.http.GET

internal interface CompanyInfoApi {

    @GET("/v3/info")
    suspend fun getCompanyInfo(): CompanyInfoDto

}