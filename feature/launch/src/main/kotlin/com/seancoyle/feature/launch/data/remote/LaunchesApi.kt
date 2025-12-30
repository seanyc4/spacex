package com.seancoyle.feature.launch.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

private const val UPCOMING_URL = "/2.3.0/launches/upcoming/?mode=list&limit=20&ordering=net"
private const val PREVIOUS_URL = "/2.3.0/launches/previous/?mode=list&limit=20&ordering=-net"
private const val OFFSET = "offset"
private const val SEARCH = "search"

internal interface LaunchApi {

    @GET(UPCOMING_URL)
    suspend fun getUpcomingLaunches(
        @Query(OFFSET) offset: Int = 0,
        @Query(SEARCH) search: String? = null,
    ): LaunchesDto

    @GET(PREVIOUS_URL)
    suspend fun getPreviousLaunches(
        @Query(OFFSET) offset: Int = 0,
        @Query(SEARCH) search: String? = null,
    ): LaunchesDto

}
