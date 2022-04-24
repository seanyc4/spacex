package com.seancoyle.movies.business.data.network.abstraction.moviedetail

import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast

interface MovieDetailNetworkDataSource {

    suspend fun getCast(movieId: String): MovieCast

}
