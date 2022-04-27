package com.seancoyle.movies.framework.datasource.network.api.movielist

import com.seancoyle.movies.framework.datasource.network.model.movielist.MovieNetworkEntity
import com.seancoyle.movies.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieListApi {

    @GET("/3/discover/movie")
    suspend fun get(
        @Query(value = "api_key") apiKey: String = API_KEY,
        @Query(value = "language") language: String = "en-US",
        @Query(value = "sort_by") sortBy: String = "primary_release_date.desc",
        @Query(value = "page") page: String = "1",
        @Query(value = "with_companies") withCompanies: String = "420|19551|38679|2301|13252",
    ): MovieNetworkEntity

}