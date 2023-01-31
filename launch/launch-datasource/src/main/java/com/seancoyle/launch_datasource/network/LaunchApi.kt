package com.seancoyle.launch_datasource.network

import com.seancoyle.launch_datasource.network.model.launch.LaunchDto
import com.seancoyle.launch_models.model.launch.LaunchOptions
import retrofit2.http.Body
import retrofit2.http.POST

interface LaunchApi {

    @POST("/v4/launches/query")
    suspend fun getLaunchList(
        @Body options: LaunchOptions
    ): LaunchDto

}