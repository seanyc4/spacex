package com.seancoyle.movies.di

import com.seancoyle.movies.framework.presentation.BaseApplication
import com.seancoyle.movies.framework.presentation.MainActivity
import com.seancoyle.movies.framework.presentation.moviedetail.MovieDetailFragment
import com.seancoyle.movies.framework.presentation.movielist.MoviesListFragment
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
        AppModule::class,
        FragmentFactoryModule::class,
        MovieDetailModule::class,
        MovieListModule::class,
        ProductionModule::class,
        ViewModelModule::class,
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory{

        fun create(@BindsInstance app: BaseApplication): AppComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(moviesListFragment: MoviesListFragment)
    fun inject(movieDetailFragment: MovieDetailFragment)
}












