package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.business.domain.state.DataState
import com.seancoyle.movies.business.interactors.moviedetail.GetMovieCastByIdFromCache.Companion.GET_MOVIE_CAST_BY_ID_SUCCESS
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
1. getMovieCastByIdFromCache_success_confirmCorrect()
    a) get a movie cast from the db with a specific id
    b) listen for GET_MOVIE_CAST_BY_ID_SUCCESS from flow emission
    c) compare the id to requested with the id of the movie we retrieved
*/
@InternalCoroutinesApi
class GeMovieCastByIdFromCacheTest {

    // system in test
    private val getMovieById: GetMovieCastByIdFromCache

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: MovieDetailCacheDataSource
    private val factory: MovieDetailFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.movieDetailCacheDataSource
        factory = dependencyContainer.movieDetailFactory
        getMovieById = GetMovieCastByIdFromCache(
            cacheDataSource = cacheDataSource
        )
    }


    @Test
    fun getMovieCastByIdFromCache_success_confirmCorrect() = runBlocking {

        val movieCastId = 1
        var retrievedMovieCast: MovieCast? = null

        getMovieById.execute(
            id = movieCastId,
            stateEvent = MovieDetailStateEvent.GetMovieCastByIdFromCacheEvent(
                id = movieCastId
            )
        ).collect(object : FlowCollector<DataState<MovieDetailViewState>?> {
            override suspend fun emit(value: DataState<MovieDetailViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    GET_MOVIE_CAST_BY_ID_SUCCESS
                )

                value?.data?.movieCast?.let { cast ->
                    retrievedMovieCast = cast
                }
            }
        })

        assertTrue { retrievedMovieCast?.id == movieCastId }
    }

}
















