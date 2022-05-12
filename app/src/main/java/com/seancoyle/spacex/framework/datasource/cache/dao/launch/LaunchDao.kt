package com.seancoyle.spacex.framework.datasource.cache.dao.launch

import androidx.room.*
import com.seancoyle.spacex.framework.datasource.cache.model.launch.LaunchCacheEntity

@Dao
interface LaunchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(launch: LaunchCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(launches: List<LaunchCacheEntity>): LongArray

    @Query("DELETE FROM launch WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    @Query("DELETE FROM launch WHERE id IN (:ids)")
    suspend fun deleteList(ids: List<Int>): Int

    @Query("DELETE FROM launch")
    suspend fun deleteAll()

    @Query("SELECT * FROM launch WHERE id = :id")
    suspend fun getById(id: Int): LaunchCacheEntity?

    @Query("SELECT * FROM launch")
    suspend fun getAll(): List<LaunchCacheEntity>?

    @Query("SELECT COUNT(*) FROM launch")
    suspend fun getTotalEntries(): Int

}












