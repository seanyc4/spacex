package com.seancoyle.movies.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seancoyle.movies.framework.datasource.cache.dao.moviedetail.MovieDetailDao
import com.seancoyle.movies.framework.datasource.cache.dao.movielist.MovieListDao
import com.seancoyle.movies.framework.datasource.cache.model.movielist.MovieCacheEntity
import com.seancoyle.movies.framework.datasource.cache.model.moviedetail.MovieCastCacheEntity
import com.seancoyle.movies.framework.datasource.cache.model.moviedetail.MovieCastTypeConverter
import com.seancoyle.movies.framework.datasource.cache.model.moviedetail.MovieCrewTypeConverter
import com.seancoyle.movies.framework.datasource.cache.model.movielist.MoviesTypeConverter

@Database(
    entities =
    [
        MovieCacheEntity::class,
        MovieCastCacheEntity::class
    ],
    version = 2
)
@TypeConverters(
    MoviesTypeConverter::class,
    MovieCrewTypeConverter::class,
    MovieCastTypeConverter::class
)
abstract class Database : RoomDatabase() {

    abstract fun movieListDao(): MovieListDao
    abstract fun movieDetailDao(): MovieDetailDao

    companion object {
        val DATABASE_NAME: String = "movie_db"
    }


}