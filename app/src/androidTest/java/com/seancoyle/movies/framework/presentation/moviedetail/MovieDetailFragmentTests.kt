package com.seancoyle.movies.framework.presentation.moviedetail

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.movies.BaseTest
import com.seancoyle.movies.R
import com.seancoyle.movies.business.domain.model.moviedetail.Cast
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.util.DateUtil
import com.seancoyle.movies.di.*
import com.seancoyle.movies.framework.datasource.cache.mappers.moviedetail.MovieDetailCacheMapper
import com.seancoyle.movies.framework.datasource.cache.mappers.movielist.MovieListCacheMapper
import com.seancoyle.movies.framework.datasource.data.moviedetail.MovieDetailDataFactory
import com.seancoyle.movies.framework.datasource.data.movielist.MovieListDataFactory
import com.seancoyle.movies.framework.presentation.TestMovieFragmentFactory
import com.seancoyle.movies.framework.presentation.UIController
import com.seancoyle.movies.framework.presentation.moviedetail.adapter.MovieCastAdapter
import com.seancoyle.movies.framework.presentation.movielist.MOVIE_DETAIL_SELECTED_MOVIE_BUNDLE_KEY
import com.seancoyle.movies.util.EspressoIdlingResourceRule
import com.seancoyle.movies.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/*
    --Test cases:
    1. Is the selected movie retrieve from the bundle args and set properly?
    2. Is the movie cast recyclerview displayed correctly and scrolling?
    3. Is the nav controller called to navigate backwards?
 */
@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@UninstallModules(
    MovieDetailModule::class,
    MovieListModule::class,
    MovieFragmentFactoryModule::class
)
@RunWith(AndroidJUnit4ClassRunner::class)
class MovieDetailFragmentTests : BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var movieListCacheMapper: MovieListCacheMapper

    @Inject
    lateinit var movieListDataFactory: MovieListDataFactory

    @Inject
    lateinit var movieDetailCacheMapper: MovieDetailCacheMapper

    @Inject
    lateinit var movieDetailDataFactory: MovieDetailDataFactory

    @Inject
    lateinit var dateUtil: DateUtil

    @Inject
    lateinit var fragmentFactory: TestMovieFragmentFactory

    private lateinit var testMovie: Movie
    private lateinit var testMovieCast: List<Cast>
    private val uiController = mockk<UIController>(relaxed = true)
    private val navController = mockk<NavController>(relaxed = true)

    @Before
    fun before() {
        hiltRule.inject()
        testMovie = movieListDataFactory.produceListOfMovies().getOrNull(0)?.movies?.getOrNull(0)!!
        testMovieCast =
            movieDetailDataFactory.produceListOfMovieCast().getOrNull(0)?.cast ?: emptyList()
        setupUIController()
    }

    private fun setupUIController() {
        fragmentFactory.uiController = uiController
    }

    /**
     * I decided to write a single large test when testing fragments in isolation.
     * Because if I make multiple tests, they have issues sharing state. I can solve
     * that issue by using test orchestrator, but that will prevent me from getting
     * reports.
     */
    @Test
    fun movieDetailFragmentTest() {

        // setup
        launchFragmentInHiltContainer<MovieDetailFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundleOf(MOVIE_DETAIL_SELECTED_MOVIE_BUNDLE_KEY to testMovie)
        ) {
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }

        // test

        // confirm arguments are set from bundle
        ("${testMovie.title} (${dateUtil.removeTimeFromDateString(testMovie.release_date)})").also {
            onView(withId(R.id.movie_title)).check(matches(withText(it)))
        }
        onView(withId(R.id.movie_overview)).check(matches(withText(testMovie.overview)))
        onView(withId(R.id.movie_detail_poster)).check(matches(isDisplayed()))

        // confirm the cast recyclerview is in view
        val movieCastRecyclerView = onView(withId(R.id.rv_movie_cast))
        movieCastRecyclerView.check(matches(isDisplayed()))

        // Scroll to the end of the horizontal cast recyclerview
        movieCastRecyclerView.perform(
            RecyclerViewActions.scrollToPosition<MovieCastAdapter.MovieCastViewHolder>(
                testMovieCast.size.minus(1)
            )
        )

        // navigate back
        Espresso.pressBack()

        // confirm NavController attempted to navigate
        verify {
            navController.popBackStack()
        }

    }
}

























