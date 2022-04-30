package com.seancoyle.movies.framework.datasource.cache.implementation.movielist

import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.framework.datasource.cache.abstraction.movielist.MovieListDaoService
import com.seancoyle.movies.framework.datasource.cache.dao.movielist.MovieListDao
import com.seancoyle.movies.framework.datasource.cache.mappers.movielist.MovieListCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieListDaoServiceImpl
@Inject
constructor(
    private val dao: MovieListDao,
    private val cacheMapper: MovieListCacheMapper
) : MovieListDaoService {

    override suspend fun insert(movies: MoviesDomainEntity): Long {
        return dao.insert(
            cacheMapper.mapToEntity(
                domainModel = movies
            )
        )
    }

    override suspend fun insertList(movies: List<MoviesDomainEntity>): LongArray {
        return dao.insertList(
            cacheMapper.domainListToEntityList(
                movies = movies
            )
        )
    }

    override suspend fun deleteList(movies: List<MoviesDomainEntity>): Int {
        val ids = movies.mapIndexed { _, movie -> movie.id  }
        return dao.deleteMovies(
            ids = ids
        )
    }

    override suspend fun deleteAll() {
        return dao.deleteAll()
    }

    override suspend fun deleteById(id: String): Int {
        return dao.deleteById(
            id = id
        )
    }

    override suspend fun getById(id: String): MoviesDomainEntity? {
        return dao.getById(id = id)?.let {
            cacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getAll(): List<MoviesDomainEntity>? {
        return dao.getAll()?.let {
            cacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun getTotalEntries(): Int {
        return dao.getTotalEntries()
    }

}













