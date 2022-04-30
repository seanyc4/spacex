package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.business.interactors.moviedetail.GetAllMovieCastFromCache.Companion.GET_ALL_MOVIE_CASTS_SUCCESS
import com.seancoyle.movies.di.DependencyContainer
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListStateEvent.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/*
Test cases:
1. getAllMovieCastFromCache_success_confirmCorrect()
    a) get the total number of movies in cache
    b) query the cache to return all items in the table
    c) listen for GET_ALL_MOVIE_CASTS_SUCCESS from flow emission
    d) compare with the total number with the results from the fake data set
*/
@InternalCoroutinesApi
class GetAllMovieCastFromCacheTest {

    // system in test
    private val getAllMovies: GetAllMovieCastFromCache

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: MovieDetailCacheDataSource
    private val factory: MovieDetailFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.movieDetailCacheDataSource
        factory = dependencyContainer.movieDetailFactory
        getAllMovies = GetAllMovieCastFromCache(
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun getAllMovieCastFromCache_success_confirmCorrect() = runBlocking {

        val numMovieCast = cacheDataSource.getTotalEntries()
        var results: ArrayList<MovieCastDomainEntity>? = null

        getAllMovies.execute(
            stateEvent = GetAllMoviesFromCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_ALL_MOVIE_CASTS_SUCCESS
            )

            value?.data?.movieCastList?.let { list ->
                results = ArrayList(list)
            }
        }

        // confirm movie casts were retrieved
        assertTrue { results != null }

        // confirm movies retrieved matches total number
        assertTrue { results?.size == numMovieCast }
    }

}
















