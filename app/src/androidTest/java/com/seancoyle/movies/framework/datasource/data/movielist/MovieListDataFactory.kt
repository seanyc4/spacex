package com.seancoyle.movies.framework.datasource.data.movielist

import android.app.Application
import android.content.res.AssetManager
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.movies.business.domain.model.movielist.Movie
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class MovieListDataFactory
@Inject
constructor(
    private val application: Application,
    private val factory: MovieListFactory
) {

    fun produceListOfMovies(): List<MovieParent> {
        val movies: List<MovieParent> = Gson()
            .fromJson(
                getMoviesFromFile("movie_list.json"),
                object : TypeToken<List<MovieParent>>() {}.type
            )
        return movies
    }

    fun produceEmptyListOfMovies(): List<MovieParent> {
        return ArrayList()
    }

    fun getMoviesFromFile(fileName: String): String? {
        return readJSONFromAsset(fileName)
    }

    private fun readJSONFromAsset(fileName: String): String? {
        var json: String? = null
        json = try {
            val inputStream: InputStream = (application.assets as AssetManager).open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun createSingleMovie(
        id: String?,
        category: String?,
        page: Int?,
        movies: List<Movie>?,
        total_pages: Int?,
        total_results: Int?
    ) = factory.createSingleMovie(
        id = id,
        category = category,
        page = page,
        movies = movies,
        total_pages = total_pages,
        total_results = total_results
    )

    fun createMovieList(numMovies: Int) = factory.createMovieList(numMovies)
}













