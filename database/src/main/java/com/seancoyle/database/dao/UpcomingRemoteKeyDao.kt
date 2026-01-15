package com.seancoyle.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.seancoyle.database.entities.UpcomingRemoteKeyEntity

@Dao
interface UpcomingRemoteKeyDao {

    @Query("SELECT * FROM upcoming_remote_keys")
    suspend fun getAll(): List<UpcomingRemoteKeyEntity?>

    @Query("SELECT * FROM upcoming_remote_keys WHERE id = :id")
    suspend fun getById(id: String): UpcomingRemoteKeyEntity?

    @Upsert
    suspend fun upsert(remoteKey: UpcomingRemoteKeyEntity)

    @Upsert
    suspend fun upsertAll(remoteKeys: List<UpcomingRemoteKeyEntity>)

    @Query("DELETE FROM upcoming_remote_keys")
    suspend fun deleteAll()
}
