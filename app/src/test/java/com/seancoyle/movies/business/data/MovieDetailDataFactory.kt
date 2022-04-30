package com.seancoyle.movies.business.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.movies.business.data.cache.moviedetail.FakeMovieDetailDatabase
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity

class MovieDetailDataFactory(
    private val testClassLoader: ClassLoader
) {

    fun produceListOfCastMembers(): List<MovieCastDomainEntity> {
        return Gson()
            .fromJson(
                getCastFromFile("cast_list.json"),
                object : TypeToken<List<MovieCastDomainEntity>>() {}.type
            )
    }

    fun produceFakeMovieDetailDatabase(castList: List<MovieCastDomainEntity>): FakeMovieDetailDatabase {
        val db = FakeMovieDetailDatabase()
        for (cast in castList) {
           db.movieCast.add(cast)
        }
        return db
    }

    fun produceEmptyListOfMovies(): List<MovieCastDomainEntity> {
        return ArrayList()
    }

    private fun getCastFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }

}


















