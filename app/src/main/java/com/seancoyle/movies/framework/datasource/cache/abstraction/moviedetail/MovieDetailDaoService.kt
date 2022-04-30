package com.seancoyle.movies.framework.datasource.cache.abstraction.moviedetail

import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity

interface MovieDetailDaoService {

    suspend fun insert(movieCast: MovieCastDomainEntity): Long

    suspend fun deleteById(id: Int): Int

    suspend fun deleteList(castList: List<MovieCastDomainEntity>) : Int

    suspend fun deleteAll()

    suspend fun getById(id: Int): MovieCastDomainEntity?

    suspend fun getAll(): List<MovieCastDomainEntity>?

    suspend fun getTotalEntries(): Int

    suspend fun insertList(castList: List<MovieCastDomainEntity>): LongArray
}






