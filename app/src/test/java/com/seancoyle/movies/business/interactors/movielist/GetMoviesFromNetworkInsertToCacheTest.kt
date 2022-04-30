package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.movielist.FakeMovieListDatabase
import com.seancoyle.movies.business.data.cache.movielist.FakeMovieListCacheDataSourceImpl
import com.seancoyle.movies.business.data.network.movielist.FakeMovieListNetworkDataSourceImpl
import com.seancoyle.movies.business.data.network.movielist.MockWebServerResponseMovieList.movieList
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.business.interactors.movielist.GetMoviesFromNetworkAndInsertToCache.Companion.MOVIES_ERROR
import com.seancoyle.movies.business.interactors.movielist.GetMoviesFromNetworkAndInsertToCache.Companion.MOVIES_INSERT_SUCCESS
import com.seancoyle.movies.di.DependencyContainer
import com.seancoyle.movies.framework.datasource.network.mappers.movielist.MovieListNetworkMapper
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListStateEvent
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection

class GetMoviesFromNetworkInsertToCacheTest {

    private val fakeMovieListDatabase = FakeMovieListDatabase()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    // system in test
    private lateinit var getMovieListFromNetworkAndInsertToCache: GetMoviesFromNetworkAndInsertToCache

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private lateinit var api: FakeMovieListNetworkDataSourceImpl
    private lateinit var dao: FakeMovieListCacheDataSourceImpl
    private lateinit var factory: MovieListFactory
    private val networkMapper = MovieListNetworkMapper()

    @BeforeEach
    fun setup() {
        dependencyContainer.build()
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("3/discover/movie/")
        api = FakeMovieListNetworkDataSourceImpl(
            baseUrl = baseUrl,
            networkMapper = networkMapper
        )

        dao = FakeMovieListCacheDataSourceImpl(
            fakeMovieListDatabase = fakeMovieListDatabase
        )

        factory = dependencyContainer.movieListFactory

        // instantiate the system in test
        getMovieListFromNetworkAndInsertToCache = GetMoviesFromNetworkAndInsertToCache(
            cacheDataSource = dao,
            movieListNetworkDataSource = api,
            factory = factory
        )
    }

    /**
     * 1. Are the movies retrieved from the network?
     * 2. Are the movies inserted into the cache?
     * 3. Check the list of movies are valid Movies?
     */
    @Test
    fun getMoviesFromNetwork_InsertToCache_GetFromCache(): Unit = runBlocking {
        var moviesResult: MoviesDomainEntity? = null

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(movieList)
        )

        // confirm the cache is empty to start
        assert(dao.getAll().isEmpty())

        // execute use case
        getMovieListFromNetworkAndInsertToCache.execute(
            stateEvent = MovieListStateEvent.GetMoviesFromNetworkAndInsertToCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                MOVIES_INSERT_SUCCESS
            ).also {
                moviesResult = value?.data?.movies
            }
        }

        // confirm the cache is no longer empty
        assert(dao.getAll().isNotEmpty())

        // Movie Result should contain a list of movies
        assert(moviesResult?.movies?.size ?: 0 > 0)

        // confirm they are actually Movie objects
        assert(moviesResult?.movies?.get(index = 0) is Movie)

    }

    /**
     * Simulate a bad request
     */
    @Test
    fun getMoviesFromNetwork_emitHttpError(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        // execute use case
        getMovieListFromNetworkAndInsertToCache.execute(
            stateEvent = MovieListStateEvent.GetMoviesFromNetworkAndInsertToCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                MOVIES_ERROR
            )
        }
    }

    @AfterEach
    fun reset() {
        mockWebServer.shutdown()
    }

}