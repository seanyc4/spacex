package com.seancoyle.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.seancoyle.database.entities.UpcomingDetailEntity

@Dao
interface UpcomingDetailDao {

    @Upsert
    suspend fun upsert(launch: UpcomingDetailEntity): Long

    @Upsert
    suspend fun upsertAll(launches: List<UpcomingDetailEntity>): LongArray

    @Query("SELECT * FROM upcoming_detail WHERE id = :id")
    suspend fun getById(id: String): UpcomingDetailEntity?

    @Query("DELETE FROM upcoming_detail")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM upcoming_detail")
    suspend fun getTotalEntries(): Int

    @Transaction
    suspend fun refreshLaunches(launches: List<UpcomingDetailEntity>) {
        deleteAll()
        upsertAll(launches)
    }
}
