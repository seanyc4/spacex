package com.seancoyle.spacex.framework.datasource.network.api.launch

import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchNetworkEntity
import retrofit2.http.GET

interface LaunchService {

    @GET("v3/launches")
    suspend fun getLaunchList(): List<LaunchNetworkEntity>

}