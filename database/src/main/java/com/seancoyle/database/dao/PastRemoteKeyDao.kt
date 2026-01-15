package com.seancoyle.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.seancoyle.database.entities.PastRemoteKeyEntity

@Dao
interface PastRemoteKeyDao {

    @Query("SELECT * FROM past_remote_keys")
    suspend fun getAll(): List<PastRemoteKeyEntity>

    @Query("SELECT * FROM past_remote_keys WHERE id = :id")
    suspend fun getById(id: String): PastRemoteKeyEntity

    @Upsert
    suspend fun upsert(remoteKey: PastRemoteKeyEntity)

    @Upsert
    suspend fun upsertAll(remoteKeys: List<PastRemoteKeyEntity>)

    @Query("DELETE FROM past_remote_keys")
    suspend fun deleteAll()
}
