package com.seancoyle.movies.business.domain.model.moviedetail

import java.util.*
import javax.inject.Singleton

@Singleton
class MovieDetailFactory {

    fun createSingleMovieCast(
        id: Int?,
        crewList: List<Crew>,
        castList: List<Cast>
    ): MovieCast {
        return MovieCast(
            id = id ?: UUID.randomUUID().hashCode(),
            crew = crewList,
            cast = castList
        )
    }

    fun createMovieCastList(numMovieCasts: Int): List<MovieCast> {
        val list: ArrayList<MovieCast> = ArrayList()
        for(i in 0 until numMovieCasts){ // exclusive on upper bound
            list.add(
                createSingleMovieCast(
                    id = UUID.randomUUID().hashCode(),
                    crewList = emptyList(),
                    castList = emptyList()
                )
            )
        }
        return list
    }

}









