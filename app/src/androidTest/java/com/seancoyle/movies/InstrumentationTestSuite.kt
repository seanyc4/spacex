package com.seancoyle.movies

import com.seancoyle.movies.framework.datasource.cache.movielist.MovieListDaoServiceTests
import com.seancoyle.movies.framework.datasource.network.movielist.MovieListRetrofitServiceTests
import com.seancoyle.movies.framework.presentation.end_to_end.MovieFeatureTest
import com.seancoyle.movies.framework.presentation.moviedetail.MovieDetailFragmentTests
import com.seancoyle.movies.framework.presentation.movielist.MoviesListFragmentTests
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite


@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    MovieListDaoServiceTests::class,
    MovieListRetrofitServiceTests::class,
    MovieDetailFragmentTests::class,
    MoviesListFragmentTests::class,
    MovieFeatureTest::class
)
class InstrumentationTestSuite

























