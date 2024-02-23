package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.implementation.data.network.dto.CompanyDto
import retrofit2.http.GET

internal interface CompanyApi {
    @GET("/v3/info")
    suspend fun getCompany(): CompanyDto
}