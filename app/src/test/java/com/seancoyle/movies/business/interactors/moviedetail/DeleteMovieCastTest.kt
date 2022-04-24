package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.CacheErrors.CACHE_ERROR_UNKNOWN
import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.data.cache.moviedetail.FORCE_DELETE_MOVIE_CAST_EXCEPTION
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.business.domain.state.DataState
import com.seancoyle.movies.business.interactors.moviedetail.DeleteMovieCast.Companion.DELETE_MOVIE_CAST_FAILED
import com.seancoyle.movies.business.interactors.moviedetail.DeleteMovieCast.Companion.DELETE_MOVIE_CAST_SUCCESS
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
1. deleteMovieCast_success()
    a) get total movie entries from db
    b) delete a movie
    c) check for success message from flow emission
    d) confirm movie was deleted from "movie" db
2. deleteMovieCast_fail_confirm()
    a) attempt to delete a movie, fail since does not exist
    b) check for failure message from flow emission
    c) confirm db was not changed
3. throwException_checkGenericError()
    a) attempt to delete a movie, force an exception to throw
    b) check for failure message from flow emission
    c) confirm db was not changed
 */
@InternalCoroutinesApi
class DeleteMovieCastTest {

    // system in test
    private val deleteMovieCast: DeleteMovieCast<MovieDetailViewState>

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: MovieDetailCacheDataSource
    private val factory: MovieDetailFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.movieDetailCacheDataSource
        factory = dependencyContainer.movieDetailFactory
        deleteMovieCast = DeleteMovieCast(
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun deleteMovieCast_success() = runBlocking {

        // choose a movie cast to delete
        val movieCastToDelete = cacheDataSource.getById(1)

        deleteMovieCast.execute(
            movieCastToDelete!!,
            MovieDetailStateEvent.DeleteMovieCastEvent(movieCastToDelete)
        ).collect(object : FlowCollector<DataState<MovieDetailViewState>?> {
            override suspend fun emit(value: DataState<MovieDetailViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_MOVIE_CAST_SUCCESS
                )
            }
        })

        // confirm was deleted
        val wasMovieCastDeleted = cacheDataSource.getById(1)
        assertTrue { wasMovieCastDeleted != movieCastToDelete }
    }

    @Test
    fun deleteMovieCast_fail() = runBlocking {

        val numMovieCastsInCacheBeforeDelete = cacheDataSource.getTotalEntries()

        // create a movie cast to delete that doesn't exist in data set
        val movieCastToDelete = MovieCast(
            id = UUID.randomUUID().hashCode(),
            cast = emptyList(),
            crew = emptyList()
        )

        deleteMovieCast.execute(
            movieCastToDelete,
            MovieDetailStateEvent.DeleteMovieCastEvent(movieCastToDelete)
        ).collect(object : FlowCollector<DataState<MovieDetailViewState>?> {
            override suspend fun emit(value: DataState<MovieDetailViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_MOVIE_CAST_FAILED
                )
            }
        })

        // confirm nothing was deleted from "movie" db
        val numMovieCastsInCacheAfterDelete = cacheDataSource.getTotalEntries()
        assertTrue { numMovieCastsInCacheBeforeDelete == numMovieCastsInCacheAfterDelete }

    }

    @Test
    fun throwException_checkGenericError() = runBlocking {

        val numMovieCastsInCacheBeforeDelete = cacheDataSource.getTotalEntries()

        // create a movie cast to delete that will throw exception
        val movieCastToDelete = MovieCast(
            id = FORCE_DELETE_MOVIE_CAST_EXCEPTION,
            cast = emptyList(),
            crew = emptyList()
        )

        deleteMovieCast.execute(
            movieCastToDelete,
            MovieDetailStateEvent.DeleteMovieCastEvent(movieCastToDelete)
        ).collect(object : FlowCollector<DataState<MovieDetailViewState>?> {
            override suspend fun emit(value: DataState<MovieDetailViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        // confirm nothing was deleted from "movie cast" db
        val numMovieCastsInCache = cacheDataSource.getTotalEntries()
        assertTrue { numMovieCastsInCache == numMovieCastsInCacheBeforeDelete }

    }

}


























