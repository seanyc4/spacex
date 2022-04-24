package com.seancoyle.movies.framework.presentation.moviedetail

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.movies.BaseTest
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.di.TestAppComponent
import com.seancoyle.movies.framework.datasource.cache.mappers.movielist.MovieListCacheMapper
import com.seancoyle.movies.framework.datasource.data.movielist.MovieListDataFactory
import com.seancoyle.movies.framework.presentation.TestMovieFragmentFactory
import com.seancoyle.movies.framework.presentation.UIController
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
import java.util.*
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

    private val testMovie: MovieParent

    val uiController = mockk<UIController>(relaxed = true)

    val navController = mockk<NavController>(relaxed = true)

    init {
        injectTest()
        testMovie = movieListDataFactory.createSingleMovie(
            id = UUID.randomUUID().toString(),
            category = UUID.randomUUID().toString(),
            page = UUID.randomUUID().hashCode(),
            movies = emptyList(),
            total_pages = UUID.randomUUID().hashCode(),
            total_results = UUID.randomUUID().hashCode()
        )
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
            fragmentArgs = bundleOf(MOVIE_DETAIL_SELECTED_MOVIE_BUNDLE_KEY to testMovie.movies.getOrNull(0))
        ).onFragment { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), navController)
                }
            }
        }

        // test

        // confirm arguments are set from bundle
       // onView(withId(R.id.movie_title)).check(matches(withText(testMovie.)))


        // confirm NavController attempted to navigate
        verify {
            navController.popBackStack()
        }

    }
}

























