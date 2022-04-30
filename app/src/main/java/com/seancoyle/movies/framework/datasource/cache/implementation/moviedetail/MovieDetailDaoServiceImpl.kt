package com.seancoyle.movies.framework.datasource.cache.implementation.moviedetail

import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.framework.datasource.cache.abstraction.moviedetail.MovieDetailDaoService
import com.seancoyle.movies.framework.datasource.cache.dao.moviedetail.MovieDetailDao
import com.seancoyle.movies.framework.datasource.cache.mappers.moviedetail.MovieDetailCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailDaoServiceImpl
@Inject
constructor(
    private val dao: MovieDetailDao,
    private val cacheMapper: MovieDetailCacheMapper
) : MovieDetailDaoService {

    override suspend fun insert(movieCast: MovieCastDomainEntity): Long {
        return dao.insert(
            cacheMapper.mapToEntity(
                domainModel = movieCast
            )
        )
    }

    override suspend fun insertList(castList: List<MovieCastDomainEntity>): LongArray {
        return dao.insertList(
            cacheMapper.domainListToEntityList(
                domainList = castList
            )
        )
    }

    override suspend fun deleteAll() {
        return dao.deleteAll()
    }

    override suspend fun deleteById(id: Int): Int {
        return dao.deleteById(
            id = id
        )
    }

    override suspend fun deleteList(castList: List<MovieCastDomainEntity>): Int {
        val ids = castList.mapIndexed { _, cast -> cast.id }
        return dao.deleteList(
            ids = ids
        )
    }

    override suspend fun getById(id: Int): MovieCastDomainEntity? {
        return dao.get(id = id)?.let {
            cacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getAll(): List<MovieCastDomainEntity>? {
        return dao.getAll()?.let {
            cacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun getTotalEntries(): Int {
        return dao.getTotalEntries()
    }

}













