package com.seancoyle.movies.framework.datasource.network.mappers.moviedetail

import com.seancoyle.movies.business.domain.model.moviedetail.Cast
import com.seancoyle.movies.business.domain.model.moviedetail.Crew
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import com.seancoyle.movies.business.domain.util.EntityMapper
import com.seancoyle.movies.framework.datasource.network.model.moviedetail.CastNetworkEntity
import com.seancoyle.movies.framework.datasource.network.model.moviedetail.CrewNetworkEntity
import com.seancoyle.movies.framework.datasource.network.model.moviedetail.MovieCastNetworkEntity


class MovieDetailNetworkMapper : EntityMapper<MovieCastNetworkEntity, MovieCast> {

    fun entityListToDomainList(entityList: List<MovieCastNetworkEntity>): List<MovieCast> {
        val list: ArrayList<MovieCast> = ArrayList()
        for (item in entityList) {
            list.add(mapFromEntity(item))
        }
        return list
    }

    fun domainListToEntityList(domainList: List<MovieCast>): List<MovieCastNetworkEntity> {
        val list: ArrayList<MovieCastNetworkEntity> = ArrayList()
        for (item in domainList) {
            list.add(mapToEntity(item))
        }
        return list
    }

    override fun mapFromEntity(entity: MovieCastNetworkEntity): MovieCast {
        return MovieCast(
            cast = entity.cast?.map { cast ->
                Cast(
                    adult = cast?.adult ?: false,
                    cast_id = cast?.cast_id ?: 0,
                    character = cast?.character ?: "",
                    credit_id = cast?.credit_id ?: "",
                    gender = cast?.gender ?: 0,
                    id = cast?.id ?: 0,
                    known_for_department = cast?.known_for_department ?: "",
                    name = cast?.name ?: "",
                    order = cast?.order ?: 0,
                    original_name = cast?.original_name ?: "",
                    popularity = cast?.popularity ?: 0.0,
                    profile_path = cast?.profile_path ?: ""
                )
            } ?: emptyList(),
            crew = entity.crew?.map { crew ->
                Crew(
                    adult = crew?.adult ?: false,
                    credit_id = crew?.credit_id ?: "",
                    gender = crew?.gender ?: 0,
                    id = crew?.id ?: 0,
                    known_for_department = crew?.known_for_department ?: "",
                    name = crew?.name ?: "",
                    original_name = crew?.original_name ?: "",
                    popularity = crew?.popularity ?: 0.0,
                    profile_path = crew?.profile_path ?: "",
                    department = crew?.department ?: "",
                    job = crew?.job ?: ""
                )
            } ?: emptyList(),
            id = entity.id ?: 0
        )
    }

    override fun mapToEntity(domainModel: MovieCast): MovieCastNetworkEntity {
        return MovieCastNetworkEntity(
            cast = domainModel.cast.map { cast ->
                CastNetworkEntity(
                    adult = cast.adult,
                    cast_id = cast.cast_id,
                    character = cast.character,
                    credit_id = cast.credit_id,
                    gender = cast.gender,
                    id = cast.id,
                    known_for_department = cast.known_for_department,
                    name = cast.name,
                    order = cast.order,
                    original_name = cast.original_name,
                    popularity = cast.popularity,
                    profile_path = cast.profile_path
                )
            },
            crew = domainModel.crew.map { crew ->
                CrewNetworkEntity(
                    adult = crew.adult,
                    credit_id = crew.credit_id,
                    gender = crew.gender,
                    id = crew.id,
                    known_for_department = crew.known_for_department,
                    name = crew.name,
                    original_name = crew.original_name,
                    popularity = crew.popularity,
                    profile_path = crew.profile_path,
                    department = crew.department,
                    job = crew.job
                )
            },
            id = domainModel.id
        )
    }

}







