package com.seancoyle.movies.framework.presentation.movielist.state

import android.os.Parcelable
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieListViewState(

    var movieParents: ArrayList<MovieParent>? = null,
    var movieParent: MovieParent? = null,
    var movie: Movie? = null,
    var movieCast: MovieCast? = null,
    var layoutManagerState: Parcelable? = null,
    var numMoviesInCache: Int? = null

): Parcelable, ViewState
























