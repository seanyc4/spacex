package com.seancoyle.movies.framework.datasource.cache.mappers.moviedetail

import com.seancoyle.movies.business.domain.model.moviedetail.Cast
import com.seancoyle.movies.business.domain.model.moviedetail.Crew
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.business.domain.util.EntityMapper
import com.seancoyle.movies.framework.datasource.cache.model.moviedetail.CastCacheEntity
import com.seancoyle.movies.framework.datasource.cache.model.moviedetail.CrewCacheEntity
import com.seancoyle.movies.framework.datasource.cache.model.moviedetail.MovieCastCacheEntity

class MovieDetailCacheMapper : EntityMapper<MovieCastCacheEntity, MovieCastDomainEntity> {

    fun entityListToDomainList(entityList: List<MovieCastCacheEntity>): List<MovieCastDomainEntity> {
        val list: ArrayList<MovieCastDomainEntity> = ArrayList()
        for (entity in entityList) {
            list.add(mapFromEntity(entity))
        }
        return list
    }

    fun domainListToEntityList(domainList: List<MovieCastDomainEntity>): List<MovieCastCacheEntity> {
        val entities: ArrayList<MovieCastCacheEntity> = ArrayList()
        for (movie in domainList) {
            entities.add(mapToEntity(movie))
        }
        return entities
    }

    override fun mapFromEntity(entity: MovieCastCacheEntity): MovieCastDomainEntity {
        return MovieCastDomainEntity(
            cast = entity.cast.map { cast ->
                Cast(
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
            crew = entity.crew.map { crew ->
                Crew(
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
            id = entity.id
        )
    }

    override fun mapToEntity(domainModel: MovieCastDomainEntity): MovieCastCacheEntity {
        return MovieCastCacheEntity(
            cast = domainModel.cast.map { cast ->
                CastCacheEntity(
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
                CrewCacheEntity(
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







