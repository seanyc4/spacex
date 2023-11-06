package com.seancoyle.launch.implementation.data.network

import com.seancoyle.launch.implementation.data.network.dto.LaunchDto
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import retrofit2.http.Body
import retrofit2.http.POST

internal interface LaunchApi {
    @POST("/v4/launches/query")
    suspend fun getLaunchList(
        @Body options: LaunchOptions
    ): LaunchDto
}