package com.seancoyle.movies.business.data.network.movielist

import com.seancoyle.movies.business.data.network.abstraction.movielist.MovieListNetworkDataSource
import com.seancoyle.movies.business.domain.model.movielist.MovieParent

class FakeMovieListNetworkDataSourceImpl
constructor(
    private val movieData: MovieParent
) : MovieListNetworkDataSource {

    override suspend fun get(): MovieParent {
        return movieData
    }

}
