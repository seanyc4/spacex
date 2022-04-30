package com.seancoyle.movies.business.data.cache.movielist

import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity

class FakeMovieListDatabase {

    // fake for movies table in local db
    val movies = mutableListOf<MoviesDomainEntity>()

}