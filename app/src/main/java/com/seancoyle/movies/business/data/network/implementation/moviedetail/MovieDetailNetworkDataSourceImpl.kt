package com.seancoyle.movies.business.data.network.implementation.moviedetail

import com.seancoyle.movies.business.data.network.abstraction.moviedetail.MovieDetailNetworkDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import com.seancoyle.movies.framework.datasource.network.abstraction.moviedetail.MovieDetailRetrofitService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieDetailNetworkDataSourceImpl
@Inject
constructor(
    private val retrofitService: MovieDetailRetrofitService
) : MovieDetailNetworkDataSource {

    override suspend fun getCast(movieId: String): MovieCast {
        return retrofitService.getCast(movieId = movieId)
    }

}





























