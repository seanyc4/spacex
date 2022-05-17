package com.seancoyle.spacex.framework.datasource.network.api.launch

import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchDto
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import retrofit2.http.Body
import retrofit2.http.POST

interface LaunchApi {

    @POST("/v4/launches/query")
    suspend fun getLaunchList(
        @Body options: LaunchOptions
    ): LaunchDto

}