package com.seancoyle.movies.framework.datasource.cache.implementation.moviedetail

import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
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

    override suspend fun insert(movieCast: MovieCast): Long {
        return dao.insert(
            cacheMapper.mapToEntity(
                domainModel = movieCast
            )
        )
    }

    override suspend fun insertList(castList: List<MovieCast>): LongArray {
        return dao.insertList(
            cacheMapper.domainListToEntityList(
                castList = castList
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

    override suspend fun deleteList(castList: List<MovieCast>): Int {
        val ids = castList.mapIndexed { _, cast -> cast.id }
        return dao.deleteList(
            ids = ids
        )
    }

    override suspend fun getById(id: Int): MovieCast? {
        return dao.get(id = id)?.let {
            cacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getAll(): List<MovieCast>? {
        return dao.getAll()?.let {
            cacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun getTotalEntries(): Int {
        return dao.getTotalEntries()
    }

}













