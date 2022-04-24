package com.seancoyle.movies.business.data.network.moviedetail

import com.seancoyle.movies.business.data.network.abstraction.moviedetail.MovieDetailNetworkDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast

class FakeMovieDetailNetworkDataSourceImpl
constructor(
    private val castData: MovieCast
) : MovieDetailNetworkDataSource {

    override suspend fun getCast(movieId: String): MovieCast {
        return castData
    }

}
