package com.seancoyle.movies.business.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast

class MovieDetailDataFactory(
    private val testClassLoader: ClassLoader
) {

    fun produceListOfCastMembers(): List<MovieCast> {
        return Gson()
            .fromJson(
                getCastFromFile("cast_list.json"),
                object : TypeToken<List<MovieCast>>() {}.type
            )
    }

    fun produceHashMapOfMovies(castList: List<MovieCast>): HashMap<Int, MovieCast> {
        val map = HashMap<Int, MovieCast>()
        for (cast in castList) {
            map[cast.id] = cast
        }
        return map
    }

    fun produceEmptyListOfMovies(): List<MovieCast> {
        return ArrayList()
    }

    fun getCastFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }

}


















