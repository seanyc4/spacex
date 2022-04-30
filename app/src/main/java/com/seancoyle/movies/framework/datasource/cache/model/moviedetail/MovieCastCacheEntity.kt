package com.seancoyle.movies.framework.datasource.cache.model.moviedetail

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "movie_cast")
data class MovieCastCacheEntity(

    @TypeConverters(MovieCastTypeConverter::class)
    val cast: List<CastCacheEntity>,

    @TypeConverters(MovieCrewTypeConverter::class)
    val crew: List<CrewCacheEntity>,

    @PrimaryKey(autoGenerate = false)
    val id: Int
)

data class CastCacheEntity(
    val adult: Boolean,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val gender: Int,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val order: Int,
    val original_name: String,
    val popularity: Double,
    val profile_path: String
)

data class CrewCacheEntity(
    val adult: Boolean,
    val credit_id: String,
    val department: String,
    val gender: Int,
    val id: Int,
    val job: String,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String
)