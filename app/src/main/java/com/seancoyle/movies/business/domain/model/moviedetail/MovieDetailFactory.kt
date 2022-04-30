package com.seancoyle.movies.business.domain.model.moviedetail

import java.util.*
import javax.inject.Singleton

@Singleton
class MovieDetailFactory {

    fun createSingleMovieCast(
        id: Int?,
        crewList: List<Crew>,
        castList: List<Cast>
    ): MovieCastDomainEntity {
        return MovieCastDomainEntity(
            id = id ?: UUID.randomUUID().hashCode(),
            crew = crewList,
            cast = castList
        )
    }

    fun createMovieCastList(numMovieCasts: Int): List<MovieCastDomainEntity> {
        val list: ArrayList<MovieCastDomainEntity> = ArrayList()
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









