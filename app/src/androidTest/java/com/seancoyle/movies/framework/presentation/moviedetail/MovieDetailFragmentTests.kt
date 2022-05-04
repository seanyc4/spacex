package com.seancoyle.movies.framework.presentation.moviedetail

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
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
import com.seancoyle.movies.di.TestAppComponent
import com.seancoyle.movies.framework.datasource.cache.mappers.moviedetail.MovieDetailCacheMapper
import com.seancoyle.movies.framework.datasource.cache.mappers.movielist.MovieListCacheMapper
import com.seancoyle.movies.framework.datasource.data.moviedetail.MovieDetailDataFactory
import com.seancoyle.movies.framework.datasource.data.movielist.MovieListDataFactory
import com.seancoyle.movies.framework.presentation.TestMovieFragmentFactory
import com.seancoyle.movies.framework.presentation.UIController
import com.seancoyle.movies.framework.presentation.moviedetail.adapter.MovieCastAdapter
import com.seancoyle.movies.framework.presentation.movielist.MOVIE_DETAIL_SELECTED_MOVIE_BUNDLE_KEY
import com.seancoyle.movies.util.EspressoIdlingResourceRule
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

 */
@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
class MovieDetailFragmentTests : BaseTest() {

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var fragmentFactory: TestMovieFragmentFactory

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

    private val testMovie: Movie
    private val testMovieCast: List<Cast>

    val uiController = mockk<UIController>(relaxed = true)
    val navController = mockk<NavController>(relaxed = true)

    init {
        injectTest()
        testMovie = movieListDataFactory.produceListOfMovies().getOrNull(0)?.movies?.getOrNull(0)!!
        testMovieCast = movieDetailDataFactory.produceListOfMovieCast().getOrNull(0)?.cast ?: emptyList()
    }

    override fun injectTest() {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }

    @Before
    fun before() {
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
        val scenario = launchFragmentInContainer<MovieDetailFragment>(
            factory = fragmentFactory,
            fragmentArgs = bundleOf(MOVIE_DETAIL_SELECTED_MOVIE_BUNDLE_KEY to testMovie)
        ).onFragment { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), navController)
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

























