package com.seancoyle.movies.business.data.network.abstraction.movielist

import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity

interface MovieListNetworkDataSource {

    suspend fun get(): MoviesDomainEntity

}
