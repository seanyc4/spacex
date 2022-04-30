package com.seancoyle.movies.business.data

import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.movies.business.data.cache.movielist.FakeMovieListDatabase

class MovieListDataFactory(
    private val testClassLoader: ClassLoader
) {

    fun produceListOfMovies(): List<MoviesDomainEntity> {
        return Gson()
            .fromJson(
                getMoviesFromFile("movie_list.json"),
                object : TypeToken<List<MoviesDomainEntity>>() {}.type
            )
    }

    fun produceFakeAppDatabase(movieList: List<MoviesDomainEntity>): FakeMovieListDatabase {
        val database = FakeMovieListDatabase()
        for (movie in movieList) {
            database.movies.add(movie)
        }
        return database
    }

    fun produceEmptyListOfMovies(): List<MoviesDomainEntity> {
        return ArrayList()
    }

    private fun getMoviesFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }

}


















