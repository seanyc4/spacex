package com.seancoyle.feature.launch.implementation.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

private const val UPCOMING_URL = "/2.3.0/launches/upcoming/?mode=list&ordering=net&limit=20"
private const val PREVIOUS_URL = "/2.3.0/launches/previous/?mode=list&ordering=net&limit=20"
private const val OFFSET = "offset"

internal interface LaunchApi {

    @GET(UPCOMING_URL)
    suspend fun getUpcomingLaunches(
        @Query(OFFSET) offset: Int = 0
    ): LaunchesDto

    @GET(PREVIOUS_URL)
    suspend fun getPreviousLaunches(
        @Query(OFFSET) offset: Int = 0
    ): LaunchesDto

}
