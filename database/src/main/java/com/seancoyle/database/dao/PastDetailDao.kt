package com.seancoyle.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.seancoyle.database.entities.PastDetailEntity

@Dao
interface PastDetailDao {

    @Upsert
    suspend fun upsert(launch: PastDetailEntity): Long

    @Upsert
    suspend fun upsertAll(launches: List<PastDetailEntity>): LongArray

    @Query("SELECT * FROM past_detail WHERE id = :id")
    suspend fun getById(id: String): PastDetailEntity?

    @Query("DELETE FROM past_detail")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM past_detail")
    suspend fun getTotalEntries(): Int

    @Transaction
    suspend fun refreshLaunches(launches: List<PastDetailEntity>) {
        deleteAll()
        upsertAll(launches)
    }
}
