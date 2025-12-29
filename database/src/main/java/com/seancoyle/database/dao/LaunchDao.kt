package com.seancoyle.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchRemoteKeyEntity

@Dao
interface LaunchDao {

    @Transaction
    suspend fun refreshLaunches(launches: List<LaunchEntity>) {
        deleteAll()
        upsertAll(launches)
    }

    @Transaction
    suspend fun refreshLaunchesWithKeys(
        launches: List<LaunchEntity>,
        remoteKeyDao: LaunchRemoteKeyDao,
        remoteKeys: List<LaunchRemoteKeyEntity>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int
    ) {
        deleteAll()
        remoteKeyDao.deleteRemoteKey()
        upsertAll(launches)
        remoteKeyDao.upsertAll(remoteKeys)
    }

    @Query("SELECT * FROM launch")
    fun pagingSource(): PagingSource<Int, LaunchEntity>

    @Upsert
    suspend fun upsert(launch: LaunchEntity): Long

    @Upsert
    suspend fun upsertAll(launches: List<LaunchEntity>): LongArray

    @Query("DELETE FROM launch")
    suspend fun deleteAll()

    @Query("SELECT * FROM launch WHERE id = :id")
    suspend fun getById(id: String): LaunchEntity?

    @Query("SELECT COUNT(*) FROM launch")
    suspend fun getTotalEntries(): Int

}