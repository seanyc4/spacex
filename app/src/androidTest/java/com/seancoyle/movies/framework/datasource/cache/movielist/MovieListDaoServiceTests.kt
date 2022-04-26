package com.seancoyle.movies.framework.datasource.cache.movielist

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.movies.BaseTest
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.di.TestAppComponent
import com.seancoyle.movies.framework.datasource.cache.abstraction.movielist.MovieListDaoService
import com.seancoyle.movies.framework.datasource.cache.dao.movielist.MovieListDao
import com.seancoyle.movies.framework.datasource.cache.implementation.movielist.MovieListDaoServiceImpl
import com.seancoyle.movies.framework.datasource.cache.mappers.movielist.MovieListCacheMapper
import com.seancoyle.movies.framework.datasource.data.movielist.MovieListDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/*
    LEGEND:
    1. CBS = "Confirm by searching"

    Test cases:
    1. confirm database movie empty to start (should be test data inserted from CacheTest.kt)
    2. insert a new movie, CBS
    3. insert a list of movies, CBS
    4. insert 1000 new movies, confirm db size increased
    5. delete new movie, confirm deleted
    6. delete list of movies, CBS

 */
@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MovieListDaoServiceTests : BaseTest() {


    // system in test
    private val movieListDaoService: MovieListDaoService

    // dependencies
    @Inject
    lateinit var dao: MovieListDao

    @Inject
    lateinit var movieListDataFactory: MovieListDataFactory

    @Inject
    lateinit var movieListFactory: MovieListFactory

    @Inject
    lateinit var movieListCacheMapper: MovieListCacheMapper

    init {
        injectTest()
        insertTestData()
        movieListDaoService = MovieListDaoServiceImpl(
            dao = dao,
            cacheMapper = movieListCacheMapper
        )
    }

    override fun injectTest() {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }

    fun insertTestData() = runBlocking {
        val entityList = movieListCacheMapper.domainListToEntityList(
            movieListDataFactory.produceListOfMovies()
        )
        dao.insertList(entityList)
    }

    /**
     * This test runs first. Check to make sure the test data was inserted from
     * CacheTest class.
     */
    @Test
    fun a_searchMovies_confirmDbNotEmpty() = runBlocking {

        val numMovies = movieListDaoService.getTotalEntries()

        assertTrue { numMovies > 0 }

    }

    @Test
    fun insertMovie_CBS() = runBlocking {

        val newMovie = movieListFactory.createSingleMovie(
            id = "1",
            category = "",
            page = 0,
            movies = emptyList(),
            total_pages = 0,
            total_results = 0
        )
        movieListDaoService.insert(newMovie)

        val movie = movieListDaoService.getById("1")
        assert(movie == newMovie)
    }

    @Test
    fun insertMovieList_CBS() = runBlocking {

        val movieList = movieListFactory.createMovieList(10)
        movieListDaoService.insertList(movieList)

        val queriedMovies = movieListDaoService.getAll()

        assertTrue { queriedMovies?.containsAll(movieList) == true }
    }

    @Test
    fun insert1000Movies_confirmNumMoviesInDb() = runBlocking {
        val currentNumMovies = movieListDaoService.getTotalEntries()

        // insert 1000 movies
        val movieList = movieListFactory.createMovieList(1000)
        movieListDaoService.insertList(movieList)

        val numMovies = movieListDaoService.getTotalEntries()
        assertEquals(currentNumMovies + 1000, numMovies)
    }

    @Test
    fun insertMovie_deleteMovie_confirmDeleted() = runBlocking {
        val newMovie = movieListFactory.createSingleMovie(
            id = "1",
            category = "",
            page = 0,
            movies = emptyList(),
            total_pages = 0,
            total_results = 0
        )
        movieListDaoService.insert(newMovie)

        var movie = movieListDaoService.getById(newMovie.id)
        assert(movie == newMovie)

        movieListDaoService.deleteById(newMovie.id)
        movie = movieListDaoService.getById(newMovie.id)
        assert(movie != newMovie)
    }

    @Test
    fun deleteMovieList_confirmDeleted() = runBlocking {
        val movies: ArrayList<MovieParent> = ArrayList(movieListDaoService.getAll())

        // select some random movies for deleting
        val moviesToDelete: ArrayList<MovieParent> = ArrayList()

        // 1st
        var movieToDelete = movies[Random.nextInt(0, movies.size - 1) + 1]
        movies.remove(movieToDelete)
        moviesToDelete.add(movieToDelete)

        // 2nd
        movieToDelete = movies[Random.nextInt(0, movies.size - 1) + 1]
        movies.remove(movieToDelete)
        moviesToDelete.add(movieToDelete)

        // 3rd
        movieToDelete = movies[Random.nextInt(0, movies.size - 1) + 1]
        movies.remove(movieToDelete)
        moviesToDelete.add(movieToDelete)

        // 4th
        movieToDelete = movies[Random.nextInt(0, movies.size - 1) + 1]
        movies.remove(movieToDelete)
        moviesToDelete.add(movieToDelete)

        movieListDaoService.deleteList(moviesToDelete)

        // confirm they were deleted
        val searchResults = movieListDaoService.getAll()
        assertFalse { searchResults == moviesToDelete }
    }

}














