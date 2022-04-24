package com.seancoyle.movies.framework.datasource.cache.dao.movielist

import androidx.room.*
import com.seancoyle.movies.framework.datasource.cache.model.movielist.MovieCacheEntity

@Dao
interface MovieListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(movies: List<MovieCacheEntity>): LongArray

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun deleteById(id: String): Int

    @Query("DELETE FROM movies WHERE id IN (:ids)")
    suspend fun deleteMovies(ids: List<String>): Int

    @Query("DELETE FROM movies")
    suspend fun deleteAll()

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getById(id: String): MovieCacheEntity?

    @Query("SELECT * FROM movies")
    suspend fun getAll(): List<MovieCacheEntity>?

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getTotalEntries(): Int

}












