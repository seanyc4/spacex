package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailViewState


class MovieDetailInteractors(

    val deleteMovieCast: DeleteMovieCast<MovieDetailViewState>,
    val deleteMultipleMovieCasts: DeleteMultipleMovieCasts,
    val getAllMovieCastFromCache: GetAllMovieCastFromCache,
    val getMovieCastByIdFromCache: GetMovieCastByIdFromCache,
    val getMoviesCastFromNetworkAndInsertToCache: GetMovieCastFromNetworkAndInsertToCache,
    val getNumMoviesCast: GetNumMoviesCast,
    val insertMovieCast: InsertMovieCast

)














