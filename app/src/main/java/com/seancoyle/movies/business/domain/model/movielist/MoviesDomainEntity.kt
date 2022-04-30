package com.seancoyle.movies.business.domain.model.movielist

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class MoviesDomainEntity(
    val id: String,
    val category: String,
    val page: Int,
    val movies: List<Movie>,
    val total_pages: Int,
    val total_results: Int,
    val created_at: String
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MoviesDomainEntity

        if (id != other.id) return false
        if (category != other.category) return false
        if (page != other.page) return false
        if (movies != other.movies) return false
        if (total_pages != other.total_pages) return false
        if (total_results != other.total_results) return false
        if (created_at != other.created_at) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + page
        result = 31 * result + movies.hashCode()
        result = 31 * result + total_pages
        result = 31 * result + total_results
        result = 31 * result + created_at.hashCode()
        return result
    }
}


@kotlinx.parcelize.Parcelize
data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (adult != other.adult) return false
        if (backdrop_path != other.backdrop_path) return false
        if (genre_ids != other.genre_ids) return false
        if (id != other.id) return false
        if (original_language != other.original_language) return false
        if (original_title != other.original_title) return false
        if (overview != other.overview) return false
        if (popularity != other.popularity) return false
        if (poster_path != other.poster_path) return false
        if (release_date != other.release_date) return false
        if (title != other.title) return false
        if (video != other.video) return false
        if (vote_average != other.vote_average) return false
        if (vote_count != other.vote_count) return false

        return true
    }

    override fun hashCode(): Int {
        var result = adult.hashCode()
        result = 31 * result + backdrop_path.hashCode()
        result = 31 * result + genre_ids.hashCode()
        result = 31 * result + id
        result = 31 * result + original_language.hashCode()
        result = 31 * result + original_title.hashCode()
        result = 31 * result + overview.hashCode()
        result = 31 * result + popularity.hashCode()
        result = 31 * result + poster_path.hashCode()
        result = 31 * result + release_date.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + video.hashCode()
        result = 31 * result + vote_average.hashCode()
        result = 31 * result + vote_count
        return result
    }
}

