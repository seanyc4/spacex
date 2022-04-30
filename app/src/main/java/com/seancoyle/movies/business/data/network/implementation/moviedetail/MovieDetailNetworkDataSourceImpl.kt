package com.seancoyle.movies.business.data.network.implementation.moviedetail

import com.seancoyle.movies.business.data.network.abstraction.moviedetail.MovieDetailNetworkDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.framework.datasource.network.abstraction.moviedetail.MovieDetailRetrofitService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieDetailNetworkDataSourceImpl
@Inject
constructor(
    private val retrofitService: MovieDetailRetrofitService
) : MovieDetailNetworkDataSource {

    override suspend fun getCast(movieId: String): MovieCastDomainEntity {
        return retrofitService.getCast(movieId = movieId)
    }

}





























