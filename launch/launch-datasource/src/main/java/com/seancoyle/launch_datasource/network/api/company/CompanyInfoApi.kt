package com.seancoyle.launch_datasource.network.api.company

import com.seancoyle.launch_datasource.network.model.company.CompanyInfoDto
import retrofit2.http.GET

interface CompanyInfoApi {

    @GET("/v3/info")
    suspend fun getCompanyInfo(): CompanyInfoDto

}