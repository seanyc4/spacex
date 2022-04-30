package com.seancoyle.movies.framework.datasource.cache.moviedetail

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.movies.BaseTest
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.di.TestAppComponent
import com.seancoyle.movies.framework.datasource.cache.abstraction.moviedetail.MovieDetailDaoService
import com.seancoyle.movies.framework.datasource.cache.dao.moviedetail.MovieDetailDao
import com.seancoyle.movies.framework.datasource.cache.implementation.moviedetail.MovieDetailDaoServiceImpl
import com.seancoyle.movies.framework.datasource.cache.mappers.moviedetail.MovieDetailCacheMapper
import com.seancoyle.movies.framework.datasource.data.moviedetail.MovieDetailDataFactory
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
    2. insert a new movie cast, CBS
    3. insert a list of movie cast, CBS
    4. insert 1000 new movie casts, confirm db size increased
    5. delete new movie cast, confirm deleted
    6. delete list of movie cast, CBS

 */
@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MovieDetailDaoServiceTests : BaseTest() {


    // system in test
    private val daoService: MovieDetailDaoService

    // dependencies
    @Inject
    lateinit var dao: MovieDetailDao

    @Inject
    lateinit var dataFactory: MovieDetailDataFactory

    @Inject
    lateinit var movieDetailFactory: MovieDetailFactory

    @Inject
    lateinit var cacheMapper: MovieDetailCacheMapper

    init {
        injectTest()
        insertTestData()
        daoService = MovieDetailDaoServiceImpl(
            dao = dao,
            cacheMapper = cacheMapper
        )
    }

    override fun injectTest() {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }

    private fun insertTestData() = runBlocking {
        val entityList = cacheMapper.domainListToEntityList(
            dataFactory.produceListOfMovieCast()
        )
        dao.insertList(entityList)
    }

    /**
     * This test runs first. Check to make sure the test data was inserted from
     * CacheTest class.
     */
    @Test
    fun a_searchMovieCast_confirmDbNotEmpty() = runBlocking {

        val numMovieCast = daoService.getTotalEntries()
        assertTrue { numMovieCast > 0 }
    }

    @Test
    fun insertMovieCast_CBS() = runBlocking {

        val newMovieCast = movieDetailFactory.createSingleMovieCast(
            id = 1,
            crewList = emptyList(),
            castList = emptyList()
        )
        daoService.insert(newMovieCast)

        val movieCast = daoService.getById(1)
        assert(movieCast == newMovieCast)
    }

    @Test
    fun insertMovieCastList_CBS() = runBlocking {

        val movieCastList = movieDetailFactory.createMovieCastList(10)
        daoService.insertList(movieCastList)

        val queriedMovieCast = daoService.getAll()

        assertTrue { queriedMovieCast?.containsAll(movieCastList) == true }
    }

    @Test
    fun insert1000MovieCast_confirmNumMovieCastsInDb() = runBlocking {
        val currentNumMovieCasts = daoService.getTotalEntries()

        // insert 1000 movie casts
        val movieCastList = movieDetailFactory.createMovieCastList(1000)
        daoService.insertList(movieCastList)

        val numMovieCast = daoService.getTotalEntries()
        assertEquals(currentNumMovieCasts + 1000, numMovieCast)
    }

    @Test
    fun insertMovieCast_deleteMovieCast_confirmDeleted() = runBlocking {
        val newMovie = movieDetailFactory.createSingleMovieCast(
            id = 1,
            crewList = emptyList(),
            castList = emptyList()
        )
        daoService.insert(newMovie)

        var movie = daoService.getById(newMovie.id)
        assert(movie == newMovie)

        daoService.deleteById(newMovie.id)
        movie = daoService.getById(newMovie.id)
        assert(movie != newMovie)
    }

    @Test
    fun deleteMovieCastList_confirmDeleted() = runBlocking {
        val movieCast: ArrayList<MovieCastDomainEntity> = ArrayList(daoService.getAll())

        // select some random movie casts for deleting
        val movieCastListToDelete: ArrayList<MovieCastDomainEntity> = ArrayList()

        // 1st
        var movieCastToDelete = movieCast[Random.nextInt(0, movieCast.size - 1) + 1]
        movieCast.remove(movieCastToDelete)
        movieCastListToDelete.add(movieCastToDelete)

        // 2nd
        movieCastToDelete = movieCast[Random.nextInt(0, movieCast.size - 1) + 1]
        movieCast.remove(movieCastToDelete)
        movieCastListToDelete.add(movieCastToDelete)

        // 3rd
        movieCastToDelete = movieCast[Random.nextInt(0, movieCast.size - 1) + 1]
        movieCast.remove(movieCastToDelete)
        movieCastListToDelete.add(movieCastToDelete)

        // 4th
        movieCastToDelete = movieCast[Random.nextInt(0, movieCast.size - 1) + 1]
        movieCast.remove(movieCastToDelete)
        movieCastListToDelete.add(movieCastToDelete)

        daoService.deleteList(movieCastListToDelete)

        // confirm they were deleted
        val searchResults = daoService.getAll()
        assertFalse { searchResults == movieCastListToDelete }
    }

}














