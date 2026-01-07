package com.seancoyle.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.seancoyle.database.entities.LaunchRemoteKeyEntity

@Dao
interface LaunchRemoteKeyDao {

    @Upsert
    suspend fun upsertAll(remoteKey: List<LaunchRemoteKeyEntity>)

    @Query("SELECT * FROM launch_remote_keys")
    suspend fun getRemoteKeys(): List<LaunchRemoteKeyEntity>

    @Query("SELECT * FROM launch_remote_keys WHERE id = :id")
    suspend fun getRemoteKey(id: String): LaunchRemoteKeyEntity?

    @Query("DELETE FROM launch_remote_keys")
    suspend fun deleteRemoteKey()

    @Query("SELECT createdAt FROM launch_remote_keys WHERE id = :id")
    suspend fun getCreationTime(id: String): Long?
}
