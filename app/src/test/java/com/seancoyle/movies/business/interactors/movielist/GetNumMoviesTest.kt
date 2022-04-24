package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.interactors.movielist.GetNumMovies.Companion.GET_NUM_MOVIES_SUCCESS
import com.seancoyle.movies.business.domain.state.DataState
import com.seancoyle.movies.di.DependencyContainer
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListStateEvent.*
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/*
Test cases:
1. getNumMovies_success_confirmCorrect()
    a) get the number of movies in cache
    b) listen for GET_NUM_MOVIES_SUCCESS from flow emission
    c) compare with the number of movies in the fake data set
*/
@InternalCoroutinesApi
class GetNumMoviesTest {

    // system in test
    private val getNumMovies: GetNumMovies

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: MovieListCacheDataSource
    private val factory: MovieListFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.movieListCacheDataSource
        factory = dependencyContainer.movieListFactory
        getNumMovies = GetNumMovies(
            cacheDataSource = cacheDataSource
        )
    }


    @Test
    fun getNumMovies_success_confirmCorrect() = runBlocking {

        var numMovies = 0
        getNumMovies.execute(
            stateEvent = GetNumMoviesInCacheEvent
        ).collect(object: FlowCollector<DataState<MovieListViewState>?>{
            override suspend fun emit(value: DataState<MovieListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    GET_NUM_MOVIES_SUCCESS
                )
                numMovies = value?.data?.numMoviesInCache?: 0
            }
        })

        val actualNumMoviesInCache = cacheDataSource.getTotalEntries()
        assertTrue { actualNumMoviesInCache == numMovies }
    }


}
















