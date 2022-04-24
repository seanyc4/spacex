package com.seancoyle.movies.framework.datasource.cache.abstraction.movielist

import com.seancoyle.movies.business.domain.model.movielist.MovieParent

interface MovieListDaoService {

    suspend fun insert(movie: MovieParent): Long

    suspend fun insertList(movies: List<MovieParent>): LongArray

    suspend fun deleteById(id: String): Int

    suspend fun deleteList(movies: List<MovieParent>) : Int

    suspend fun deleteAll()

    suspend fun getById(id: String): MovieParent?

    suspend fun getAll(): List<MovieParent>?

    suspend fun getTotalEntries(): Int

}












