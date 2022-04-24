package com.seancoyle.movies.di

import com.seancoyle.movies.business.data.MovieDetailDataFactory
import com.seancoyle.movies.business.data.MovieListDataFactory
import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.data.cache.moviedetail.FakeMovieDetailCacheDataSourceImpl
import com.seancoyle.movies.business.data.cache.movielist.FakeMovieListCacheDataSourceImpl
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.domain.util.DateUtil
import com.seancoyle.movies.util.isUnitTest
import java.text.SimpleDateFormat
import java.util.*

class DependencyContainer {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
    val dateUtil = DateUtil(dateFormat)
    lateinit var movieListCacheDataSource: MovieListCacheDataSource
    lateinit var movieDetailCacheDataSource: MovieDetailCacheDataSource
    lateinit var movieListFactory: MovieListFactory
    lateinit var movieDetailFactory: MovieDetailFactory
    lateinit var movieListDataFactory: MovieListDataFactory
    lateinit var movieDetailDataFactory: MovieDetailDataFactory

    init {
        isUnitTest = true // for Logger.kt
    }

    fun build() {

        this.javaClass.classLoader?.let { classLoader ->
            movieListDataFactory = MovieListDataFactory(classLoader)
        }

        this.javaClass.classLoader?.let { classLoader ->
            movieDetailDataFactory = MovieDetailDataFactory(classLoader)
        }

        movieListFactory = MovieListFactory(dateUtil)
        movieDetailFactory = MovieDetailFactory()

         movieListCacheDataSource = FakeMovieListCacheDataSourceImpl(
              moviesData = movieListDataFactory.produceHashMapOfMovies(
                  movieListDataFactory.produceListOfMovies()
              ),
          )

          movieDetailCacheDataSource = FakeMovieDetailCacheDataSourceImpl(
              castData = movieDetailDataFactory.produceHashMapOfMovies(
                  movieDetailDataFactory.produceListOfCastMembers()
              )
          )

    }

}

















