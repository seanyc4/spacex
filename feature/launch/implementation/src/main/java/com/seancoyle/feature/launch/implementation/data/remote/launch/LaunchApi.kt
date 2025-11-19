package com.seancoyle.feature.launch.implementation.data.remote.launch

import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import retrofit2.http.Body
import retrofit2.http.POST

internal interface LaunchApi {

    companion object {
        private const val URL = "/v4/launches/query"
    }

    @POST(URL)
    suspend fun getLaunches(
        @Body options: LaunchOptions
    ): LaunchesDto
}
