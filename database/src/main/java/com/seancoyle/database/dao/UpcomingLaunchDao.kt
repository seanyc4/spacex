package com.seancoyle.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.seancoyle.database.entities.UpcomingLaunchEntity
import com.seancoyle.database.entities.UpcomingRemoteKeyEntity

@Dao
interface UpcomingLaunchDao {

    @Query("SELECT * FROM upcoming_launches")
    fun pagingSource(): PagingSource<Int, UpcomingLaunchEntity>

    @Upsert
    suspend fun upsert(launch: UpcomingLaunchEntity): Long

    @Upsert
    suspend fun upsertAll(launches: List<UpcomingLaunchEntity>): LongArray

    @Query("DELETE FROM upcoming_launches")
    suspend fun deleteAll()

    @Query("SELECT * FROM upcoming_launches WHERE id = :id")
    suspend fun getById(id: String): UpcomingLaunchEntity?

    @Query("SELECT COUNT(*) FROM upcoming_launches")
    suspend fun getTotalEntries(): Int

    @Transaction
    suspend fun refreshLaunchesWithKeys(
        launches: List<UpcomingLaunchEntity>,
        remoteKeyDao: UpcomingRemoteKeyDao,
        remoteKeys: List<UpcomingRemoteKeyEntity>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int
    ) {
        deleteAll()
        remoteKeyDao.deleteAll()
        upsertAll(launches)
        remoteKeyDao.upsertAll(remoteKeys)
    }

    // Used for Pull-to-refresh
    @Transaction
    suspend fun refreshUpcomingLaunches(launches: List<UpcomingLaunchEntity>) {
        deleteAll()
        upsertAll(launches)
    }
}
