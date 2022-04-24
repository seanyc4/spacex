package com.seancoyle.movies.framework.datasource.cache.mappers.movielist

import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.util.EntityMapper
import com.seancoyle.movies.framework.datasource.cache.model.movielist.MovieCacheEntity
import com.seancoyle.movies.framework.datasource.cache.model.movielist.ResultCache

class MovieListCacheMapper : EntityMapper<MovieCacheEntity, MovieParent> {

    fun entityListToDomainList(entities: List<MovieCacheEntity>): List<MovieParent> {
        val list: ArrayList<MovieParent> = ArrayList()
        for (entity in entities) {
            list.add(mapFromEntity(entity))
        }
        return list
    }

    fun domainListToEntityList(movies: List<MovieParent>): List<MovieCacheEntity> {
        val entities: ArrayList<MovieCacheEntity> = ArrayList()
        for (movie in movies) {
            entities.add(mapToEntity(movie))
        }
        return entities
    }

    override fun mapFromEntity(entity: MovieCacheEntity): MovieParent {
        return MovieParent(
            id = entity.id,
            category = entity.category,
            created_at = entity.created_at,
            page = entity.page,
            total_results = entity.total_results,
            total_pages = entity.total_pages,
            movies = entity.results.map { result ->
                Movie(
                    adult = result.adult,
                    backdrop_path = result.backdrop_path,
                    genre_ids = result.genre_ids,
                    id = result.id,
                    original_language = result.original_language,
                    original_title = result.original_title,
                    overview = result.overview,
                    popularity = result.popularity,
                    poster_path = result.poster_path,
                    release_date = result.release_date,
                    title = result.title,
                    video = result.video,
                    vote_average = result.vote_average,
                    vote_count = result.vote_count
                )
            }
        )
    }

    override fun mapToEntity(domainModel: MovieParent): MovieCacheEntity {
        return MovieCacheEntity(
            id = domainModel.id,
            category = domainModel.category,
            created_at = domainModel.created_at,
            page = domainModel.page,
            total_results = domainModel.total_results,
            total_pages = domainModel.total_pages,
            results = domainModel.movies.map { result ->
                ResultCache(
                    adult = result.adult,
                    backdrop_path = result.backdrop_path,
                    genre_ids = result.genre_ids,
                    id = result.id,
                    original_language = result.original_language,
                    original_title = result.original_title,
                    overview = result.overview,
                    popularity = result.popularity,
                    poster_path = result.poster_path,
                    release_date = result.release_date,
                    title = result.title,
                    video = result.video,
                    vote_average = result.vote_average,
                    vote_count = result.vote_count
                )
            }
        )
    }

}







