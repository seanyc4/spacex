package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.CacheErrors.CACHE_ERROR_UNKNOWN
import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.data.cache.movielist.FORCE_DELETE_MOVIE_EXCEPTION
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.interactors.movielist.DeleteMovie.Companion.DELETE_MOVIE_FAILED
import com.seancoyle.movies.business.interactors.movielist.DeleteMovie.Companion.DELETE_MOVIE_SUCCESS
import com.seancoyle.movies.di.DependencyContainer
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListStateEvent.*
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

/*
Test cases:
1. deleteMovie_success()
    a) get total movie entries from db
    b) delete a movie
    c) check for success message from flow emission
    d) confirm movie was deleted from "movie" db
2. deleteMovie_fail_confirm()
    a) attempt to delete a movie, fail since does not exist
    b) check for failure message from flow emission
    c) confirm db was not changed
3. throwException_checkGenericError()
    a) attempt to delete a movie, force an exception to throw
    b) check for failure message from flow emission
    c) confirm db was not changed
 */
@InternalCoroutinesApi
class DeleteMovieTest {

    // system in test
    private val deleteMovie: DeleteMovie<MovieListViewState>

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: MovieListCacheDataSource
    private val factory: MovieListFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.movieListCacheDataSource
        factory = dependencyContainer.movieListFactory
        deleteMovie = DeleteMovie(
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun deleteMovie_success() = runBlocking {

        // choose a movie at random to delete
        val movieToDelete = cacheDataSource.getById("1")

        deleteMovie.execute(
            movieToDelete!!,
            DeleteMovieEvent(movieToDelete)
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                DELETE_MOVIE_SUCCESS
            )
        }

        // confirm was deleted
        val doesMovieExist = cacheDataSource.getAll()?.contains(movieToDelete)
        assertTrue { doesMovieExist == false }
    }

    @Test
    fun deleteMovie_fail() = runBlocking {

        // checks total db size before trying to delete
        val numMoviesInCacheBeforeDelete = cacheDataSource.getTotalEntries()

        // create a movie to delete that doesn't exist in data set
        val movieToDelete = MoviesDomainEntity(
            id = UUID.randomUUID().toString(),
            category = UUID.randomUUID().toString(),
            page = UUID.randomUUID().hashCode(),
            movies = emptyList(),
            total_pages = UUID.randomUUID().hashCode(),
            total_results = UUID.randomUUID().hashCode(),
            created_at = UUID.randomUUID().toString()
        )

        deleteMovie.execute(
            movieToDelete,
            DeleteMovieEvent(movieToDelete)
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                DELETE_MOVIE_FAILED
            )
        }

        // confirm nothing was deleted from "movie" db
        val numMoviesInCacheAfterDelete = cacheDataSource.getTotalEntries()
        assertTrue { numMoviesInCacheBeforeDelete == numMoviesInCacheAfterDelete }

    }

    @Test
    fun throwException_checkGenericError() = runBlocking {

        val numMoviesInCacheBeforeDelete = cacheDataSource.getTotalEntries()

        // create a movie to delete that will throw exception
        val movieToDelete = MoviesDomainEntity(
            id = FORCE_DELETE_MOVIE_EXCEPTION,
            category = UUID.randomUUID().toString(),
            page = UUID.randomUUID().hashCode(),
            movies = emptyList(),
            total_pages = UUID.randomUUID().hashCode(),
            total_results = UUID.randomUUID().hashCode(),
            created_at = UUID.randomUUID().toString()
        )


        deleteMovie.execute(
            movieToDelete,
            DeleteMovieEvent(movieToDelete)
        ).collect { value ->
            assert(
                value?.stateMessage?.response?.message
                    ?.contains(CACHE_ERROR_UNKNOWN) ?: false
            )
        }

        // confirm nothing was deleted from "movies" db
        val numMoviesInCache = cacheDataSource.getTotalEntries()
        assertTrue { numMoviesInCache == numMoviesInCacheBeforeDelete }

    }

}


























