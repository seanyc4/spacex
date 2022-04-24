package com.seancoyle.movies.framework.datasource.network.api.moviedetail

import com.seancoyle.movies.framework.datasource.network.model.moviedetail.MovieCastNetworkEntity
import com.seancoyle.movies.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDetailApi {

    @GET("/3/movie/{movie_id}/credits")
    suspend fun getCast(
        @Path(value = "movie_id") movieId: String,
        @Query(value = "api_key") apiKey: String = API_KEY,
        @Query(value = "language") language: String = "en-US",
    ): MovieCastNetworkEntity

}