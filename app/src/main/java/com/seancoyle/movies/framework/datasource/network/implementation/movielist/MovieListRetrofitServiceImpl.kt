package com.seancoyle.movies.framework.datasource.network.implementation.movielist

import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.framework.datasource.network.abstraction.movielist.MovieListRetrofitService
import com.seancoyle.movies.framework.datasource.network.mappers.movielist.MovieListNetworkMapper
import com.seancoyle.movies.framework.datasource.network.api.movielist.MovieListApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieListRetrofitServiceImpl
@Inject
constructor(
    private val api: MovieListApi,
    private val networkMapper: MovieListNetworkMapper
) : MovieListRetrofitService {

    override suspend fun get(): MovieParent {
        return networkMapper.mapFromEntity(
            api.get()
        )
    }

}












