package com.seancoyle.movies.framework.datasource.cache.abstraction.movielist

import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity

interface MovieListDaoService {

    suspend fun insert(movies: MoviesDomainEntity): Long

    suspend fun insertList(movies: List<MoviesDomainEntity>): LongArray

    suspend fun deleteById(id: String): Int

    suspend fun deleteList(movies: List<MoviesDomainEntity>) : Int

    suspend fun deleteAll()

    suspend fun getById(id: String): MoviesDomainEntity?

    suspend fun getAll(): List<MoviesDomainEntity>?

    suspend fun getTotalEntries(): Int

}












