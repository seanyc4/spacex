package com.seancoyle.movies.business.data.cache.implementation.movielist

import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.framework.datasource.cache.abstraction.movielist.MovieListDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieListCacheDataSourceImpl
@Inject
constructor(
    private val daoService: MovieListDaoService
) : MovieListCacheDataSource {

    override suspend fun insert(movie: MovieParent): Long {
        return daoService.insert(
            movie = movie
        )
    }

    override suspend fun deleteById(id: String): Int {
        return daoService.deleteById(
            id = id
        )
    }

    override suspend fun deleteList(movies: List<MovieParent>): Int {
        return daoService.deleteList(
            movies = movies
        )
    }

    override suspend fun deleteAll() {
        return daoService.deleteAll()
    }

    override suspend fun getAll(): List<MovieParent>? {
        return daoService.getAll()
    }

    override suspend fun getById(id: String): MovieParent? {
        return daoService.getById(
            id = id
        )
    }

    override suspend fun getTotalEntries(): Int {
        return daoService.getTotalEntries()
    }

    override suspend fun insertMovies(movies: List<MovieParent>): LongArray {
        return daoService.insertList(
            movies = movies
        )
    }

}





















