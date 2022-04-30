package com.seancoyle.movies.business.data.cache.abstraction.movielist

import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity

interface MovieListCacheDataSource {

    suspend fun insert(movies: MoviesDomainEntity): Long

    suspend fun deleteById(id: String): Int

    suspend fun deleteList(movies: List<MoviesDomainEntity>): Int

    suspend fun deleteAll()

    suspend fun getById(id: String): MoviesDomainEntity?

    suspend fun getAll(): List<MoviesDomainEntity>?

    suspend fun getTotalEntries(): Int

    suspend fun insertMovies(movies: List<MoviesDomainEntity>): LongArray
}






