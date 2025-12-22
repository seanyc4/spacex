package com.seancoyle.feature.launch.implementation.data.remote

import com.seancoyle.core.domain.Order
import retrofit2.http.GET
import retrofit2.http.Query

private const val UPCOMING_URL = "/2.3.0/launches/upcoming/?mode=list&limit=20"
private const val PREVIOUS_URL = "/2.3.0/launches/previous/?mode=list&limit=20"
private const val OFFSET = "offset"
private const val SEARCH = "search"
private const val ORDERING = "ordering"

internal interface LaunchApi {

    @GET(UPCOMING_URL)
    suspend fun getUpcomingLaunches(
        @Query(OFFSET) offset: Int = 0,
        @Query(SEARCH) search: String? = null,
        @Query(ORDERING) ordering: String? = Order.DESC.value
    ): LaunchesDto

    @GET(PREVIOUS_URL)
    suspend fun getPreviousLaunches(
        @Query(OFFSET) offset: Int = 0,
        @Query(SEARCH) search: String? = null,
        @Query(ORDERING) ordering: String? = Order.DESC.value
    ): LaunchesDto

}
