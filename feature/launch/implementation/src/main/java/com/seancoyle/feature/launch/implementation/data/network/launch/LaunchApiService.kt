package com.seancoyle.feature.launch.implementation.data.network.launch

import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import retrofit2.http.Body
import retrofit2.http.POST

internal interface LaunchApiService {
    companion object {
        private const val URL = "/v4/launches/query"
    }

    @POST(URL)
    suspend fun getLaunches(
        @Body options: LaunchOptions
    ): LaunchesDto?
}