package com.seancoyle.movies.framework.presentation.movielist

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.movies.BaseTest
import com.seancoyle.movies.R
import com.seancoyle.movies.di.MovieFragmentFactoryModule
import com.seancoyle.movies.di.MovieListModule
import com.seancoyle.movies.framework.datasource.cache.dao.movielist.MovieListDao
import com.seancoyle.movies.framework.datasource.cache.mappers.movielist.MovieListCacheMapper
import com.seancoyle.movies.framework.datasource.cache.model.movielist.MovieCacheEntity
import com.seancoyle.movies.framework.datasource.data.movielist.MovieListDataFactory
import com.seancoyle.movies.framework.presentation.TestMovieFragmentFactory
import com.seancoyle.movies.framework.presentation.UIController
import com.seancoyle.movies.framework.presentation.movielist.adapter.MovieListAdapter.MovieListViewHolder
import com.seancoyle.movies.util.EspressoIdlingResourceRule
import com.seancoyle.movies.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import io.mockk.verify
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

    1) check contents of movie list is displayed on screen
    2) scroll to the end of the movie list
    3) select a movie and check the navigation function gets called

*/
@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@UninstallModules(
    MovieListModule::class,
    MovieFragmentFactoryModule::class
)
@RunWith(AndroidJUnit4ClassRunner::class)
class MoviesListFragmentTests: BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var movieListCacheMapper: MovieListCacheMapper

    @Inject
    lateinit var movieListDataFactory: MovieListDataFactory

    @Inject
    lateinit var dao: MovieListDao

    @Inject
    lateinit var fragmentFactory: TestMovieFragmentFactory

    private lateinit var testMovieList: List<MovieCacheEntity>
    private val uiController = mockk<UIController>(relaxed = true)
    private val navController = mockk<NavController>(relaxed = true)

    @Before
    fun before(){
        hiltRule.inject()
        testMovieList = movieListCacheMapper.domainListToEntityList(
            movieListDataFactory.produceListOfMovies()
        )
        prepareDataSet(testMovieList)
        setupUIController()
    }

    private fun setupUIController(){
        fragmentFactory.uiController = uiController
    }

    // ** Must clear network and cache so there is no previous state issues **
    private fun prepareDataSet(testData: List<MovieCacheEntity>) = runBlocking{
        // clear any existing data so recyclerview isn't overwhelmed
        dao.deleteAll()
        dao.insertList(testData)
    }

    /**
     * I decided to write a single large test when testing fragments in isolation.
     * Because if I make multiple tests, they have issues sharing state. I can solve
     * that issue by using test orchestrator, but that will prevent me from getting
     * reports.
     */
    @Test
    fun movieListFragmentTest() = runBlocking{

        // setup
        launchFragmentInHiltContainer<MoviesListFragment>(
            fragmentFactory = fragmentFactory
        ){
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }

        // check contents of the movie list is displayed on screen
        val recyclerView = onView(withId(R.id.rv_movie_list))
        recyclerView.check(matches(isDisplayed()))

        // test scrolling to the bottom of the list
        recyclerView.perform(
            scrollToPosition<MovieListViewHolder>(testMovieList[0].results.size.minus(1))
        )

        // select a movie and confirm navigate function was called
        val selectedPosition = 1
        recyclerView.perform(
            actionOnItemAtPosition<MovieListViewHolder>(selectedPosition, click())
        )
        verify {

            if(navController.currentDestination?.id == R.id.moviesListFragment) {
                navController.navigate(
                    R.id.action_movieListFragment_to_movieDetailFragment,
                    any()
                )
            }
        }
    }
}























