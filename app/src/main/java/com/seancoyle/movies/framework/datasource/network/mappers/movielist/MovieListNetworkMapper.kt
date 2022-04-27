package com.seancoyle.movies.framework.datasource.network.mappers.movielist

import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.business.domain.util.EntityMapper
import com.seancoyle.movies.framework.datasource.network.model.movielist.MovieNetworkEntity
import com.seancoyle.movies.framework.datasource.network.model.movielist.ResultNetwork


class MovieListNetworkMapper : EntityMapper<MovieNetworkEntity, MovieParent> {

    override fun mapFromEntity(entity: MovieNetworkEntity): MovieParent {
        return MovieParent(
            id = "1",
            category = "",
            page = entity.page ?: 0,
            total_pages = entity.total_pages ?: 0,
            total_results = entity.total_results ?: 0,
            created_at = "",
            movies = entity.results?.map { result ->
                Movie(
                    adult = result.adult ?: false,
                    backdrop_path = result.backdrop_path ?: "",
                    genre_ids = result.genre_ids ?: emptyList(),
                    id = result.id ?: 0,
                    original_language = result.original_language ?: "",
                    original_title = result.original_title ?: "",
                    overview = result.overview ?: "",
                    popularity = result.popularity ?: 0.0,
                    poster_path = result.poster_path ?: "",
                    release_date = result.release_date ?: "",
                    title = result.title ?: "",
                    video = result.video ?: false,
                    vote_average = result.vote_average ?: 0.0,
                    vote_count = result.vote_count ?: 0
                )
            } ?: emptyList()
        )
    }

    override fun mapToEntity(domainModel: MovieParent): MovieNetworkEntity {
        return MovieNetworkEntity(
            page = domainModel.page,
            total_pages = domainModel.total_pages,
            total_results = domainModel.total_results,
            results = domainModel.movies.map { result ->
                ResultNetwork(
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







