package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.business.interactors.movielist.GetAllMoviesFromCache.Companion.GET_ALL_MOVIES_SUCCESS
import com.seancoyle.movies.di.DependencyContainer
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListStateEvent.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/*
Test cases:
1. getAllMoviesFromCache_success_confirmCorrect()
    a) get the total number of movies in cache
    b) query the cache to return all items in the table
    c) listen for GET_ALL_MOVIES_SUCCESS from flow emission
    d) compare with the total number with the results from the fake data set
*/
@InternalCoroutinesApi
class GetAllMoviesFromCacheTest {

    // system in test
    private val getAllMovies: GetAllMoviesFromCache

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: MovieListCacheDataSource
    private val factory: MovieListFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.movieListCacheDataSource
        factory = dependencyContainer.movieListFactory
        getAllMovies = GetAllMoviesFromCache(
            cacheDataSource = cacheDataSource
        )
    }


    @Test
    fun getAllMoviesFromCache_success_confirmCorrect() = runBlocking {

        val numMovies = cacheDataSource.getTotalEntries()
        var results: ArrayList<MoviesDomainEntity>? = null

        getAllMovies.execute(
            stateEvent = GetAllMoviesFromCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_ALL_MOVIES_SUCCESS
            )

            value?.data?.movieList?.let { list ->
                results = ArrayList(list)
            }
        }

        // confirm movies were retrieved
        assertTrue { results != null }

        // confirm movies retrieved matches total number
        assertTrue { results?.size == numMovies }
    }

}
















