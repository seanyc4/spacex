package com.seancoyle.movies.framework.presentation.common

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.seancoyle.movies.business.domain.util.DateUtil
import com.seancoyle.movies.framework.presentation.moviedetail.MovieDetailFragment
import com.seancoyle.movies.framework.presentation.movielist.MoviesListFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class MovieFragmentFactory : FragmentFactory(){

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when(className){

            MoviesListFragment::class.java.name -> {
                MoviesListFragment()
            }

            MovieDetailFragment::class.java.name -> {
                 MovieDetailFragment()
            }

            else -> {
                super.instantiate(classLoader, className)
            }
        }
}