package com.seancoyle.movies.framework.datasource.network.abstraction.moviedetail

import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity

interface MovieDetailRetrofitService {

    suspend fun getCast(movieId: String): MovieCastDomainEntity

}