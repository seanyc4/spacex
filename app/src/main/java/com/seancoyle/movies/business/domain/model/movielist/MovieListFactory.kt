package com.seancoyle.movies.business.domain.model.movielist

import com.seancoyle.movies.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieListFactory
@Inject
constructor(
    private val dateUtil: DateUtil
) {

    fun createSingleMovie(
        id: String?,
        category: String?,
        page: Int?,
        movies: List<Movie>?,
        total_pages: Int?,
        total_results: Int?
    ): MovieParent {
        return MovieParent(
            id = id ?: UUID.randomUUID().toString(),
            category = category ?: "",
            created_at = dateUtil.getCurrentTimestamp(),
            page = page ?: 0,
            movies = movies ?: emptyList(),
            total_pages = total_pages ?: 0,
            total_results = total_results ?: 0
        )
    }

    fun createMovieList(numMovies: Int): List<MovieParent> {
        val list: ArrayList<MovieParent> = ArrayList()
        for(i in 0 until numMovies){ // exclusive on upper bound
            list.add(
                createSingleMovie(
                    id = UUID.randomUUID().toString(),
                    category =  "",
                    page =  0,
                    movies =  emptyList(),
                    total_pages =  0,
                    total_results =  0
                )
            )
        }
        return list
    }

}









