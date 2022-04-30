package com.seancoyle.movies.framework.presentation.movielist.state

import android.os.Parcelable
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieListViewState(

    var movieList: ArrayList<MoviesDomainEntity>? = null,
    var movies: MoviesDomainEntity? = null,
    var movie: Movie? = null,
    var layoutManagerState: Parcelable? = null,
    var numMoviesInCache: Int? = null

): Parcelable, ViewState
























