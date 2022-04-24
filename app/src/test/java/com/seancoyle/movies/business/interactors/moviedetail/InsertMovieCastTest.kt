package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.CacheErrors
import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.data.cache.moviedetail.FORCE_NEW_MOVIE_CAST_EXCEPTION
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.business.domain.state.DataState
import com.seancoyle.movies.business.interactors.moviedetail.InsertMovieCast.Companion.INSERT_MOVIE_CAST_SUCCESS
import com.seancoyle.movies.di.DependencyContainer
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailStateEvent
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

/*
Test cases:
1. insertMovieCast_success()
    a) insert a new movie cast
    b) listen for INSERT_MOVIE_CAST_SUCCESS emission from flow
    c) confirm cache was updated with new movie cast
2. insertMovieCast_fail()
    a) insert a new movie cast
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_MOVIE_FAILED emission from flow
    e) confirm cache was not updated
3. throwException_checkGenericError()
    a) insert a new movie cast
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
 */
@InternalCoroutinesApi
class InsertMovieCastTest {

    // system in test
    private val insertMovieCast: InsertMovieCast

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: MovieDetailCacheDataSource
    private val factory: MovieDetailFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.movieDetailCacheDataSource
        factory = dependencyContainer.movieDetailFactory
        insertMovieCast = InsertMovieCast(
            movieDetailCacheDataSource = cacheDataSource,
            movieDetailFactory = factory
        )
    }

    @Test
    fun insertMovieCast_success() = runBlocking {

        val newMovieCast = factory.createSingleMovieCast(
            id = UUID.randomUUID().hashCode(),
            crewList = emptyList(),
            castList = emptyList()
        )

        insertMovieCast.execute(
            id = newMovieCast.id,
            crewList = newMovieCast.crew,
            castList = newMovieCast.cast,
            stateEvent = MovieDetailStateEvent.InsertMovieCastEvent(
                id = newMovieCast.id,
                crewList = newMovieCast.crew,
                castList = newMovieCast.cast,
            )
        ).collect(object : FlowCollector<DataState<MovieDetailViewState>?> {
            override suspend fun emit(value: DataState<MovieDetailViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    INSERT_MOVIE_CAST_SUCCESS
                )
            }
        })

        // confirm cache was updated
        val cacheMovieCastThatWasInserted = cacheDataSource.getById(newMovieCast.id)
        assertTrue { cacheMovieCastThatWasInserted == newMovieCast }
    }

    @Test
    fun insertMovieCast_fail() = runBlocking {

        val newMovieCast = factory.createSingleMovieCast(
            id = com.seancoyle.movies.business.data.cache.moviedetail.FORCE_GENERAL_FAILURE,
            crewList = emptyList(),
            castList = emptyList()
        )

        insertMovieCast.execute(
            id = newMovieCast.id,
            crewList = newMovieCast.crew,
            castList = newMovieCast.cast,
            stateEvent = MovieDetailStateEvent.InsertMovieCastEvent(
                id = newMovieCast.id,
                crewList = newMovieCast.crew,
                castList = newMovieCast.cast,
            )
        ).collect(object : FlowCollector<DataState<MovieDetailViewState>?> {
            override suspend fun emit(value: DataState<MovieDetailViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    InsertMovieCast.INSERT_MOVIE_CAST_FAILED
                )
            }
        })

        // confirm cache was not changed
        val cacheMovieCastThatWasInserted = cacheDataSource.getById(newMovieCast.id)
        assertTrue { cacheMovieCastThatWasInserted == null }
    }

    @Test
    fun throwException_checkGenericError() = runBlocking {

        val newMovieCast = factory.createSingleMovieCast(
            id = FORCE_NEW_MOVIE_CAST_EXCEPTION,
            crewList = emptyList(),
            castList = emptyList()
        )

        insertMovieCast.execute(
            id = newMovieCast.id,
            crewList = newMovieCast.crew,
            castList = newMovieCast.cast,
            stateEvent = MovieDetailStateEvent.InsertMovieCastEvent(
                id = newMovieCast.id,
                crewList = newMovieCast.crew,
                castList = newMovieCast.cast,
            )
        ).collect(object : FlowCollector<DataState<MovieDetailViewState>?> {
            override suspend fun emit(value: DataState<MovieDetailViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        // confirm cache was not changed
        val cacheMovieCastThatWasInserted = cacheDataSource.getById(newMovieCast.id)
        assertTrue { cacheMovieCastThatWasInserted == null }
    }
}





















