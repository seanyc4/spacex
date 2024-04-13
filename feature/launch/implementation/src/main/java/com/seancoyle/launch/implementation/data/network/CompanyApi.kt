package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.implementation.data.network.dto.CompanyDto
import retrofit2.http.GET

internal interface CompanyApi {
    companion object {
        private const val URL = "/v4/company"
    }

    @GET(URL)
    suspend fun getCompany(): CompanyDto
}