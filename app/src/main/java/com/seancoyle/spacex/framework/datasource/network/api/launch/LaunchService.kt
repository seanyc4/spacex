package com.seancoyle.spacex.framework.datasource.network.api.launch

import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchNetworkEntity
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import retrofit2.http.Body
import retrofit2.http.POST

interface LaunchService {

    @POST("/v4/launches/query")
    suspend fun getLaunchList(
        @Body options: LaunchOptions
    ): LaunchNetworkEntity

}