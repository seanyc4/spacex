package com.seancoyle.movies.framework.presentation.moviedetail.state

import android.os.Parcelable
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.business.domain.state.ViewState

@kotlinx.parcelize.Parcelize
data class MovieDetailViewState(

    var movieCast: MovieCastDomainEntity? = null,
    var movieCastList: List<MovieCastDomainEntity>? = null,
    var movie: Movie?= null,
    var numMoviesCast: Int? = null

) : Parcelable, ViewState




















