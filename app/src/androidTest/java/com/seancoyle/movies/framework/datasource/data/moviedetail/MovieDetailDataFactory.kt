package com.seancoyle.movies.framework.datasource.data.moviedetail

import android.app.Application
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.movies.business.domain.model.moviedetail.Cast
import com.seancoyle.movies.business.domain.model.moviedetail.Crew
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class MovieDetailDataFactory
@Inject
constructor(
    private val application: Application,
    private val factory: MovieDetailFactory
) {

    fun produceListOfMovieCast(): List<MovieCast> {
        return Gson()
            .fromJson(
                getMovieCastFromFile("cast_list.json"),
                object : TypeToken<List<MovieCast>>() {}.type
            )
    }

    fun produceEmptyListOfMovieCast(): List<MovieCast> {
        return ArrayList()
    }

    fun getMovieCastFromFile(fileName: String): String? {
        return readJSONFromAsset(fileName)
    }

    private fun readJSONFromAsset(fileName: String): String? {
        val json: String? = try {
            val inputStream: InputStream = (application.assets as AssetManager).open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun createMovieCast(
        id: Int?,
        crewList: List<Crew>,
        castList: List<Cast>
    ) = factory.createSingleMovieCast(
        id = id,
        crewList = crewList,
        castList = castList
    )

    fun createMovieCast(numMovies: Int) = factory.createMovieCastList(numMovies)
}













