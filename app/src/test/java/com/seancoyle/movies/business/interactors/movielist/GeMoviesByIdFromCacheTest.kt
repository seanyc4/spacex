package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.business.interactors.movielist.GetMovieByIdFromCache.Companion.GET_MOVIE_BY_ID_SUCCESS
import com.seancoyle.movies.di.DependencyContainer
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListStateEvent.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/*
Test cases:
1. getMoviesByIdFromCache_success_confirmCorrect()
    a) get a movie from the db with a specific id
    b) listen for GET_MOVIE_BY_ID_SUCCESS from flow emission
    c) compare the id to requested with the id of the movie we retrieved
*/
@InternalCoroutinesApi
class GeMoviesByIdFromCacheTest {

    // system in test
    private val getMovieById: GetMovieByIdFromCache

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: MovieListCacheDataSource
    private val factory: MovieListFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.movieListCacheDataSource
        factory = dependencyContainer.movieListFactory
        getMovieById = GetMovieByIdFromCache(
            cacheDataSource = cacheDataSource
        )
    }


    @Test
    fun getMoviesByIdFromCache_success_confirmCorrect() = runBlocking {

        val movieId = "1"
        var retrievedMovies: MoviesDomainEntity? = null

        getMovieById.execute(
            id = movieId,
            stateEvent = GetMovieByIdFromCacheEvent(
                id = movieId
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_MOVIE_BY_ID_SUCCESS
            )

            value?.data?.movies?.let { movie ->
                retrievedMovies = movie
            }
        }

        assertTrue { retrievedMovies?.id == movieId }
    }

}
















