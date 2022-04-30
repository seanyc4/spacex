package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.moviedetail.FakeMovieDetailCacheDataSourceImpl
import com.seancoyle.movies.business.data.cache.moviedetail.FakeMovieDetailDatabase
import com.seancoyle.movies.business.data.network.moviedetail.FakeMovieDetailNetworkDataSourceImpl
import com.seancoyle.movies.business.data.network.moviedetail.MockWebServerResponseMovieDetail.movieCast
import com.seancoyle.movies.business.domain.model.moviedetail.Cast
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.business.interactors.moviedetail.GetMovieCastFromNetworkAndInsertToCache.Companion.MOVIE_CAST_ERROR
import com.seancoyle.movies.business.interactors.moviedetail.GetMovieCastFromNetworkAndInsertToCache.Companion.MOVIE_CAST_INSERT_SUCCESS
import com.seancoyle.movies.di.DependencyContainer
import com.seancoyle.movies.framework.datasource.network.mappers.moviedetail.MovieDetailNetworkMapper
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailStateEvent
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection

class GetMovieCastFromNetworkInsertToCacheTest {

    private val fakeMovieDetailDatabase = FakeMovieDetailDatabase()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    // system in test
    private lateinit var getMovieCastFromNetworkInsertToCache: GetMovieCastFromNetworkAndInsertToCache

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private lateinit var api: FakeMovieDetailNetworkDataSourceImpl
    private lateinit var dao: FakeMovieDetailCacheDataSourceImpl
    private lateinit var factory: MovieDetailFactory
    private val networkMapper = MovieDetailNetworkMapper()

    @BeforeEach
    fun setup() {
        dependencyContainer.build()
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("3/movie/")
        api = FakeMovieDetailNetworkDataSourceImpl(
            baseUrl = baseUrl,
            networkMapper = networkMapper
        )

        dao = FakeMovieDetailCacheDataSourceImpl(
            fakeMovieDetailDatabase = fakeMovieDetailDatabase
        )

        factory = dependencyContainer.movieDetailFactory

        // instantiate the system in test
        getMovieCastFromNetworkInsertToCache = GetMovieCastFromNetworkAndInsertToCache(
            cacheDataSource = dao,
            networkDataSource = api,
            factory = factory
        )
    }

    /**
     * 1. Are the movie cast retrieved from the network?
     * 2. Are the movie cast inserted into the cache?
     * 3. Check the list of movie cast are valid MovieCastDomainEntity?
     */
    @Test
    fun getMovieCastFromNetwork_InsertToCache_GetFromCache(): Unit = runBlocking {
        var movieResult: MovieCastDomainEntity? = null

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(movieCast)
        )

        // confirm the cache is empty to start
        assert(dao.getAll().isEmpty())

        // execute use case
        getMovieCastFromNetworkInsertToCache.execute(
            movieId = "447365",
            stateEvent = MovieDetailStateEvent.GetMovieCastFromNetworkAndInsertToCacheEvent(
                movieId = "447365"
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                MOVIE_CAST_INSERT_SUCCESS
            ).also {
                movieResult = value?.data?.movieCast
            }
        }

        // confirm the cache is no longer empty
        assert(dao.getAll().isNotEmpty())

        // Movie Result should contain a list of movies
        assert(movieResult?.cast?.size ?: 0 > 0)

        // confirm they are actually Movie objects
        assert(movieResult?.cast?.get(index = 0) is Cast)

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
        getMovieCastFromNetworkInsertToCache.execute(
            movieId = "",
            stateEvent = MovieDetailStateEvent.GetMovieCastFromNetworkAndInsertToCacheEvent(
                movieId = ""
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                MOVIE_CAST_ERROR
            )
        }
    }

    @AfterEach
    fun reset() {
        mockWebServer.shutdown()
    }

}