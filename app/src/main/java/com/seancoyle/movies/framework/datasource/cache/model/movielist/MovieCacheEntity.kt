package com.seancoyle.movies.framework.datasource.cache.model.movielist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "movies")
data class MovieCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "page")
    val page: Int,

    @TypeConverters(MoviesTypeConverter::class)
    val results: List<ResultCache>,

    @ColumnInfo(name = "total_pages")
    val total_pages: Int,

    @ColumnInfo(name = "total_results")
    val total_results: Int,

    @ColumnInfo(name = "created_at")
    val created_at: String

)

data class ResultCache(
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
)



