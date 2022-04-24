package com.seancoyle.movies.business.data.network.abstraction.movielist

import com.seancoyle.movies.business.domain.model.movielist.MovieParent

interface MovieListNetworkDataSource {

    suspend fun get(): MovieParent

}
