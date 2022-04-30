package com.seancoyle.movies.framework.datasource.network.implementation.moviedetail

import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.framework.datasource.network.abstraction.moviedetail.MovieDetailRetrofitService
import com.seancoyle.movies.framework.datasource.network.api.moviedetail.MovieDetailApi
import com.seancoyle.movies.framework.datasource.network.mappers.moviedetail.MovieDetailNetworkMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieCastRetrofitServiceImpl
@Inject
constructor(
    private val api: MovieDetailApi,
    private val networkMapper: MovieDetailNetworkMapper
) : MovieDetailRetrofitService {

    override suspend fun getCast(movieId: String): MovieCastDomainEntity {
        return networkMapper.mapFromEntity(
            api.getCast(movieId = movieId)
        )
    }
}












