package com.seancoyle.movies.framework.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.domain.util.DateUtil
import com.seancoyle.movies.business.interactors.moviedetail.MovieDetailInteractors
import com.seancoyle.movies.business.interactors.movielist.MovieListInteractors
import com.seancoyle.movies.framework.presentation.moviedetail.MovieDetailViewModel
import com.seancoyle.movies.framework.presentation.movielist.MovieListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
class ViewModelFactory
constructor(
    private val dateUtil: DateUtil,
    private val movieDetailInteractors: MovieDetailInteractors,
    private val movieListInteractors: MovieListInteractors,
    private val movieListFactory: MovieListFactory,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {

            MovieListViewModel::class.java -> {
                MovieListViewModel(
                    movieListInteractors = movieListInteractors,
                    movieListFactory = movieListFactory
                ) as T
            }

            MovieDetailViewModel::class.java -> {
                MovieDetailViewModel(
                    movieDetailInteractors = movieDetailInteractors,
                    dateUtil = dateUtil
                ) as T
            }

            else -> {
                throw IllegalArgumentException("unknown model class $modelClass")
            }
        }
    }
}




















