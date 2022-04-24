package com.seancoyle.movies.di

import com.seancoyle.movies.framework.datasource.cache.movielist.MovieListDaoServiceTests
import com.seancoyle.movies.framework.datasource.network.movielist.MovieListRetrofitServiceTests
import com.seancoyle.movies.framework.presentation.TestBaseApplication
import com.seancoyle.movies.framework.presentation.end_to_end.MovieFeatureTest
import com.seancoyle.movies.framework.presentation.moviedetail.MovieDetailFragmentTests
import com.seancoyle.movies.framework.presentation.movielist.MoviesListFragmentTests
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
@Component(
    modules = [
        TestModule::class,
        AppModule::class,
        MovieListModule::class,
        MovieDetailModule::class,
        TestMovieFragmentFactoryModule::class,
        ViewModelModule::class
    ]
)
interface TestAppComponent : AppComponent {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance app: TestBaseApplication): TestAppComponent
    }

    fun inject(movieListDaoServiceTests: MovieListDaoServiceTests)
    fun inject(movieListRetrofitServiceTests: MovieListRetrofitServiceTests)
    fun inject(moviesListFragmentTests: MoviesListFragmentTests)
    fun inject(movieDetailFragmentTests: MovieDetailFragmentTests)
    fun inject(movieFeatureTest: MovieFeatureTest)

}
















