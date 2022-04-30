package com.seancoyle.movies.business.data.network.implementation.movielist

import com.seancoyle.movies.business.data.network.abstraction.movielist.MovieListNetworkDataSource
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.framework.datasource.network.abstraction.movielist.MovieListRetrofitService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieListNetworkDataSourceImpl
@Inject
constructor(
    private val retrofitService: MovieListRetrofitService
) : MovieListNetworkDataSource {

    override suspend fun get(): MoviesDomainEntity {
        return retrofitService.get()
    }

}





























