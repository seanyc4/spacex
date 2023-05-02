package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.api.model.LaunchOptions
import com.seancoyle.launch.implementation.data.model.LaunchDto
import retrofit2.http.Body
import retrofit2.http.POST

interface LaunchApi {

    @POST("/v4/launches/query")
    suspend fun getLaunchList(
        @Body options: LaunchOptions
    ): LaunchDto

}