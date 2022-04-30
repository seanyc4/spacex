package com.seancoyle.movies.framework.datasource.network.abstraction.movielist

import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity

interface MovieListRetrofitService {

    suspend fun get(): MoviesDomainEntity

}