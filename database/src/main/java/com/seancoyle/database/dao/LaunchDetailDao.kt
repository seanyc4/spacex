package com.seancoyle.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.seancoyle.database.entities.LaunchEntity

@Dao
interface LaunchDetailDao {

    @Upsert
    suspend fun upsert(launch: LaunchEntity): Long

    @Upsert
    suspend fun upsertAll(launches: List<LaunchEntity>): LongArray

    @Query("SELECT * FROM launch WHERE id = :id")
    suspend fun getById(id: String): LaunchEntity?

    @Query("DELETE FROM launch")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM launch")
    suspend fun getTotalEntries(): Int
}
