package com.seancoyle.movies.framework.datasource.network.movielist

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.movies.BaseTest
import com.seancoyle.movies.di.TestAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.runner.RunWith

/*
    LEGEND:
    1. CBS = "Confirm by searching"

    Test cases:
    1. insert a single movie, CBS


 */
@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
class MovieListRetrofitServiceTests: BaseTest(){

   /* // system in test
    private lateinit var movieListRetrofitService: MovieListRetrofitService

    // dependencies
    @Inject
    lateinit var movieListDataFactory: MovieListDataFactory

    @Inject
    lateinit var movieListFactory: MovieListFactory

    @Inject
    lateinit var networkMapper: MovieListNetworkMapper

    init {
        injectTest()
    }*/

    override fun injectTest() {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }

}





































