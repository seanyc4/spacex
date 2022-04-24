package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.business.domain.state.DataState
import com.seancoyle.movies.business.interactors.moviedetail.GetNumMoviesCast.Companion.GET_NUM_MOVIE_CAST_SUCCESS
import com.seancoyle.movies.di.DependencyContainer
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailStateEvent
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailViewState
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
class GetNumMovieCastsTest {

    // system in test
    private val getNumMovieCasts: GetNumMoviesCast

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: MovieDetailCacheDataSource
    private val factory: MovieDetailFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.movieDetailCacheDataSource
        factory = dependencyContainer.movieDetailFactory
        getNumMovieCasts = GetNumMoviesCast(
            cacheDataSource = cacheDataSource
        )
    }


    @Test
    fun getNumMovieCasts_success_confirmCorrect() = runBlocking {

        var numMovieCasts = 0
        getNumMovieCasts.execute(
            stateEvent = MovieDetailStateEvent.GetNumMovieCastInCacheEvent
        ).collect(object: FlowCollector<DataState<MovieDetailViewState>?>{
            override suspend fun emit(value: DataState<MovieDetailViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    GET_NUM_MOVIE_CAST_SUCCESS
                )
                numMovieCasts = value?.data?.numMoviesCast?: 0
            }
        })

        val actualNumMovieCastsInCache = cacheDataSource.getTotalEntries()
        assertTrue { actualNumMovieCastsInCache == numMovieCasts }
    }


}
















