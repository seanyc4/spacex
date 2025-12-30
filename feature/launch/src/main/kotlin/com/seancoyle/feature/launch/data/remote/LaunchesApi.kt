package com.seancoyle.feature.launch.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val UPCOMING_LAUNCHES_URL = "/2.3.0/launches/upcoming/?mode=list&limit=20&ordering=net"
private const val PREVIOUS_LAUNCHES_URL = "/2.3.0/launches/previous/?mode=list&limit=20&ordering=-net"
private const val UPCOMING_LAUNCH_URL = "/2.3.0/launches/upcoming/?mode=detailed"
private const val PREVIOUS_LAUNCH_URL = "/2.3.0/launches/previous/?mode=detailed"
private const val OFFSET = "offset"
private const val SEARCH = "search"
private const val ID = "id"

internal interface LaunchApi {

    @GET(UPCOMING_LAUNCHES_URL)
    suspend fun getUpcomingLaunches(
        @Query(OFFSET) offset: Int = 0,
        @Query(SEARCH) search: String? = null,
    ): LaunchesDto

    @GET(PREVIOUS_LAUNCHES_URL)
    suspend fun getPreviousLaunches(
        @Query(OFFSET) offset: Int = 0,
        @Query(SEARCH) search: String? = null,
    ): LaunchesDto

    @GET(UPCOMING_LAUNCH_URL)
    suspend fun getUpcomingLaunch(
        @Path(ID) id: String,
    ): LaunchesDto

    @GET(PREVIOUS_LAUNCH_URL)
    suspend fun getPreviousLaunch(
        @Path(ID) id: String,
    ): LaunchesDto
}
