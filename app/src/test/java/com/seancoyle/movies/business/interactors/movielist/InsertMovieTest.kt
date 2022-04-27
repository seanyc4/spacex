package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.CacheErrors
import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.data.cache.movielist.FORCE_GENERAL_FAILURE
import com.seancoyle.movies.business.data.cache.movielist.FORCE_NEW_MOVIE_EXCEPTION
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.interactors.movielist.InsertMovie.Companion.INSERT_MOVIE_SUCCESS
import com.seancoyle.movies.business.domain.state.DataState
import com.seancoyle.movies.di.DependencyContainer
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListStateEvent.*
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

/*
Test cases:
1. insertMovie_success()
    a) insert a new movie
    b) listen for INSERT_MOVIE_SUCCESS emission from flow
    c) confirm cache was updated with new movie
2. insertMovie_fail()
    a) insert a new movie
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_MOVIE_FAILED emission from flow
    e) confirm cache was not updated
3. throwException_checkGenericError()
    a) insert a new movie
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
 */
@InternalCoroutinesApi
class InsertMovieTest {

    // system in test
    private val insertMovie: InsertMovie

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: MovieListCacheDataSource
    private val factory: MovieListFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.movieListCacheDataSource
        factory = dependencyContainer.movieListFactory
        insertMovie = InsertMovie(
            cacheDataSource = cacheDataSource,
            factory = factory
        )
    }

    @Test
    fun insertMovie_success() = runBlocking {

        val newMovie = factory.createSingleMovie(
            id = UUID.randomUUID().toString(),
            category = UUID.randomUUID().toString(),
            page = UUID.randomUUID().hashCode(),
            movies = emptyList(),
            total_pages = UUID.randomUUID().hashCode(),
            total_results = UUID.randomUUID().hashCode()
        )

        insertMovie.execute(
            id = newMovie.id,
            category = newMovie.category,
            page = newMovie.page,
            movies = newMovie.movies,
            total_pages = newMovie.total_pages,
            total_results = newMovie.total_results,
            stateEvent = InsertNewMovieEvent(
                id = newMovie.id,
                category = newMovie.category,
                page = newMovie.page,
                movies = newMovie.movies,
                total_pages = newMovie.total_pages,
                total_results = newMovie.total_results,
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                INSERT_MOVIE_SUCCESS
            )
        }

        // confirm cache was updated
        val cacheMovieThatWasInserted = cacheDataSource.getById(newMovie.id)
        assertTrue { cacheMovieThatWasInserted == newMovie }
    }

    @Test
    fun insertMovie_fail() = runBlocking {

        val newMovie = factory.createSingleMovie(
            id = FORCE_GENERAL_FAILURE,
            category = UUID.randomUUID().toString(),
            page = UUID.randomUUID().hashCode(),
            movies = emptyList(),
            total_pages = UUID.randomUUID().hashCode(),
            total_results = UUID.randomUUID().hashCode()
        )

        insertMovie.execute(
            id = newMovie.id,
            category = newMovie.category,
            page = newMovie.page,
            movies = newMovie.movies,
            total_pages = newMovie.total_pages,
            total_results = newMovie.total_results,
            stateEvent = InsertNewMovieEvent(
                id = newMovie.id,
                category = newMovie.category,
                page = newMovie.page,
                movies = newMovie.movies,
                total_pages = newMovie.total_pages,
                total_results = newMovie.total_results,
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                InsertMovie.INSERT_MOVIE_FAILED
            )
        }

        // confirm cache was not changed
        val cacheMovieThatWasInserted = cacheDataSource.getById(newMovie.id)
        assertTrue { cacheMovieThatWasInserted == null }
    }

    @Test
    fun throwException_checkGenericError() = runBlocking {

        val newMovie = factory.createSingleMovie(
            id = FORCE_NEW_MOVIE_EXCEPTION,
            category = UUID.randomUUID().toString(),
            page = UUID.randomUUID().hashCode(),
            movies = emptyList(),
            total_pages = UUID.randomUUID().hashCode(),
            total_results = UUID.randomUUID().hashCode()
        )

        insertMovie.execute(
            id = newMovie.id,
            category = newMovie.category,
            page = newMovie.page,
            movies = newMovie.movies,
            total_pages = newMovie.total_pages,
            total_results = newMovie.total_results,
            stateEvent = InsertNewMovieEvent(
                id = newMovie.id,
                category = newMovie.category,
                page = newMovie.page,
                movies = newMovie.movies,
                total_pages = newMovie.total_pages,
                total_results = newMovie.total_results,
            )
        ).collect { value ->
            assert(
                value?.stateMessage?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
            )
        }

        // confirm cache was not changed
        val cacheMovieThatWasInserted = cacheDataSource.getById(newMovie.id)
        assertTrue { cacheMovieThatWasInserted == null }
    }
}





















