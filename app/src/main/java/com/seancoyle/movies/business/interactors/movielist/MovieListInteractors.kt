package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState


class MovieListInteractors (
    val deleteMovie: DeleteMovie<MovieListViewState>,
    val deleteMultipleMovies: DeleteMultipleMovies,
    val getAllMoviesFromCache: GetAllMoviesFromCache,
    val getMovieByIdFromCache: GetMovieByIdFromCache,
    val getMoviesFromNetworkAndInsertToCache: GetMoviesFromNetworkAndInsertToCache,
    val getNumMovies: GetNumMovies,
    val insertMovie: InsertMovie,

    )














