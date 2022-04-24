package com.seancoyle.movies.business.data.cache.abstraction.movielist

import com.seancoyle.movies.business.domain.model.movielist.MovieParent

interface MovieListCacheDataSource {

    suspend fun insert(movie: MovieParent): Long

    suspend fun deleteById(id: String): Int

    suspend fun deleteList(movies: List<MovieParent>): Int

    suspend fun deleteAll()

    suspend fun getById(id: String): MovieParent?

    suspend fun getAll(): List<MovieParent>?

    suspend fun getTotalEntries(): Int

    suspend fun insertMovies(movies: List<MovieParent>): LongArray
}






