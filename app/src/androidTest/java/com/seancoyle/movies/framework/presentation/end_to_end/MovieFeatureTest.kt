package com.seancoyle.movies.framework.presentation.end_to_end

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.movies.BaseTest
import com.seancoyle.movies.R
import com.seancoyle.movies.business.data.network.abstraction.moviedetail.MovieDetailNetworkDataSource
import com.seancoyle.movies.business.data.network.abstraction.movielist.MovieListNetworkDataSource
import com.seancoyle.movies.di.AppModule
import com.seancoyle.movies.di.MovieDetailModule
import com.seancoyle.movies.di.MovieListModule
import com.seancoyle.movies.di.ProductionModule
import com.seancoyle.movies.framework.datasource.cache.dao.moviedetail.MovieDetailDao
import com.seancoyle.movies.framework.datasource.cache.dao.movielist.MovieListDao
import com.seancoyle.movies.framework.datasource.cache.mappers.moviedetail.MovieDetailCacheMapper
import com.seancoyle.movies.framework.datasource.cache.mappers.movielist.MovieListCacheMapper
import com.seancoyle.movies.framework.datasource.cache.model.moviedetail.MovieCastCacheEntity
import com.seancoyle.movies.framework.datasource.cache.model.movielist.MovieCacheEntity
import com.seancoyle.movies.framework.datasource.data.moviedetail.MovieDetailDataFactory
import com.seancoyle.movies.framework.datasource.data.movielist.MovieListDataFactory
import com.seancoyle.movies.framework.presentation.MainActivity
import com.seancoyle.movies.framework.presentation.moviedetail.adapter.MovieCastAdapter
import com.seancoyle.movies.framework.presentation.movielist.adapter.MovieListAdapter.*
import com.seancoyle.movies.util.EspressoIdlingResourceRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/*
    --Test cases:
    1. Navigate MoviesListFragment, confirm list is visible
    2. Scroll to the end of the list.
    3. Select a movie from list, navigate to MovieDetailFragment
    4. Confirm the movie title and overview matches the movie selected
    5. Scroll the horizontal cast recyclerview to end of the list
    6. Navigate BACK, confirm MoviesListFragment in view
 */

@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@LargeTest
@UninstallModules(
    MovieListModule::class,
    MovieDetailModule::class
)
class MovieFeatureTest: BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var movieListCacheMapper: MovieListCacheMapper

    @Inject
    lateinit var movieDetailCacheMapper: MovieDetailCacheMapper

    @Inject
    lateinit var movieListDataFactory: MovieListDataFactory

    @Inject
    lateinit var movieDetailDataFactory: MovieDetailDataFactory

    @Inject
    lateinit var movieListDao: MovieListDao

    @Inject
    lateinit var movieDetailDao: MovieDetailDao

    @Inject
    lateinit var movieListNetworkDataSource: MovieListNetworkDataSource

    @Inject
    lateinit var movieDetailNetworkDataSource: MovieDetailNetworkDataSource

    private lateinit var testMovieList: List<MovieCacheEntity>
    private lateinit var testMovieCastList: List<MovieCastCacheEntity>

    @Before
    fun init() {
        hiltRule.inject()
        testMovieList = movieListCacheMapper.domainListToEntityList(
            movieListDataFactory.produceListOfMovies()
        )
        testMovieCastList = movieDetailCacheMapper.domainListToEntityList(
            movieDetailDataFactory.produceListOfMovieCast()
        )
        prepareDataSet()
    }

    // ** Must clear network and cache so there is no previous state issues **
    private fun prepareDataSet() = runBlocking{
        // clear any existing data so recyclerview isn't overwhelmed
        movieListDao.deleteAll()
        movieListDao.insertList(testMovieList)
        movieDetailDao.deleteAll()
        movieDetailDao.insertList(testMovieCastList)
    }

    @Test
    fun generalEndToEndTest(){

        val scenario = launchActivity<MainActivity>()

        // Wait for MoviesListFragment to come into view
        waitViewShown(withId(R.id.rv_movie_list))

        val movieListRecyclerView = onView(withId(R.id.rv_movie_list))

        // confirm MoviesListFragment is in view
        movieListRecyclerView.check(matches(isDisplayed()))

        // Scroll to bottom of the list
        movieListRecyclerView.perform(
            scrollToPosition<MovieListViewHolder>(testMovieList[0].results.size.minus(1))
        )

        // Select a movie from the list
        movieListRecyclerView.perform(
            actionOnItemAtPosition<MovieListViewHolder>(1, click())
        )

        // Wait for MovieDetailFragment to come into view
        waitViewShown(withId(R.id.movie_detail_container))

        // Confirm MovieDetailFragment is in view
        onView(withId(R.id.movie_title)).check(matches(withText("Guardians of the Galaxy Vol. 3 (2023)")))
        onView(withId(R.id.movie_overview)).check(matches(withText("The third and final film in the Guardians of the Galaxy trilogy.")))
        onView(withId(R.id.movie_detail_poster)).check(matches(isDisplayed()))

        // confirm MoviesCastFragment is in view
        val movieCastRecyclerView = onView(withId(R.id.rv_movie_cast))
        movieCastRecyclerView.check(matches(isDisplayed()))

        // Scroll to the end of the horizontal cast recyclerview
        movieCastRecyclerView.perform(
            scrollToPosition<MovieCastAdapter.MovieCastViewHolder>(testMovieCastList[0].cast.size.minus(1))
        )

        // press hardware back button
        Espresso.pressBack()

        // confirm MoviesListFragment is in view
        movieListRecyclerView.check(matches(isDisplayed()))
    }

}



























