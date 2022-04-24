package com.seancoyle.movies.business.data

import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieListDataFactory(
    private val testClassLoader: ClassLoader
) {

    fun produceListOfMovies(): List<MovieParent> {
        val movies: List<MovieParent> = Gson()
            .fromJson(
                getMoviesFromFile("movie_list.json"),
                object: TypeToken<List<MovieParent>>() {}.type
            )
        return movies
    }

    fun produceHashMapOfMovies(movieList: List<MovieParent>): HashMap<String, MovieParent> {
        val map = HashMap<String, MovieParent>()
        for (movie in movieList) {
            map[movie.id] = movie
        }
        return map
    }

    fun produceEmptyListOfMovies(): List<MovieParent> {
        return ArrayList()
    }

     fun getMoviesFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }

}


















