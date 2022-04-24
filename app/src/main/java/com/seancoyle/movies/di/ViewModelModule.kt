package com.seancoyle.movies.di

import androidx.lifecycle.ViewModelProvider
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.domain.util.DateUtil
import com.seancoyle.movies.business.interactors.moviedetail.MovieDetailInteractors
import com.seancoyle.movies.business.interactors.movielist.MovieListInteractors
import com.seancoyle.movies.framework.presentation.common.ViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Module
object ViewModelModule {

    @Singleton
    @Provides
    fun provideViewModelFactory(
        dateUtil: DateUtil,
        movieDetailInteractors: MovieDetailInteractors,
        movieListInteractors: MovieListInteractors,
        movieListFactory: MovieListFactory
    ): ViewModelProvider.Factory {
        return ViewModelFactory(
            dateUtil = dateUtil,
            movieDetailInteractors = movieDetailInteractors,
            movieListInteractors = movieListInteractors,
            movieListFactory = movieListFactory,
        )
    }

}

















