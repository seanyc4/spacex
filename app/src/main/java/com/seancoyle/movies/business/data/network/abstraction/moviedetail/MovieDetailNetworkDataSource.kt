package com.seancoyle.movies.business.data.network.abstraction.moviedetail

import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity

interface MovieDetailNetworkDataSource {

    suspend fun getCast(movieId: String): MovieCastDomainEntity

}
