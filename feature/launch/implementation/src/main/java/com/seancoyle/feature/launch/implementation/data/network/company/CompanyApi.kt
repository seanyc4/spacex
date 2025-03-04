package com.seancoyle.feature.launch.implementation.data.network.company

import retrofit2.http.GET

internal interface CompanyApi {
    companion object {
        const val COMPANY_URL = "/v4/company"
    }

    @GET(COMPANY_URL)
    suspend fun getCompany(): CompanyDto?
}