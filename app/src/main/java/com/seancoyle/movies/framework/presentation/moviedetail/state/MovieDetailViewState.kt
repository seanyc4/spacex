package com.seancoyle.movies.framework.presentation.moviedetail.state

import android.os.Parcelable
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import com.seancoyle.movies.business.domain.state.ViewState

@kotlinx.parcelize.Parcelize
data class MovieDetailViewState(

    var movieCast: MovieCast? = null,
    var movieCastList: List<MovieCast>? = null,
    var movie: Movie?= null,
    var numMoviesCast: Int? = null

) : Parcelable, ViewState




















