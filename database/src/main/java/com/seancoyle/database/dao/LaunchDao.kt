package com.seancoyle.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.seancoyle.database.entities.LaunchRemoteKeyEntity
import com.seancoyle.database.entities.LaunchSummaryEntity

@Dao
interface LaunchDao {

    @Transaction
    suspend fun refreshLaunches(launches: List<LaunchSummaryEntity>) {
        deleteAll()
        upsertAll(launches)
    }

    @Transaction
    suspend fun refreshLaunchesWithKeys(
        launches: List<LaunchSummaryEntity>,
        remoteKeyDao: LaunchRemoteKeyDao,
        remoteKeys: List<LaunchRemoteKeyEntity>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int
    ) {
        // Only delete launches for this specific launch type
        if (launches.isNotEmpty()) {
            val launchType = launches.first().launchType
            deleteByLaunchType(launchType)
        }
        remoteKeyDao.deleteRemoteKey()
        upsertAll(launches)
        remoteKeyDao.upsertAll(remoteKeys)
    }

    @Query("SELECT * FROM launch_summary")
    fun pagingSource(): PagingSource<Int, LaunchSummaryEntity>

    @Query("SELECT * FROM launch_summary WHERE launch_type = :launchType")
    fun pagingSourceByType(launchType: String): PagingSource<Int, LaunchSummaryEntity>

    @Upsert
    suspend fun upsert(launch: LaunchSummaryEntity): Long

    @Upsert
    suspend fun upsertAll(launches: List<LaunchSummaryEntity>): LongArray

    @Query("DELETE FROM launch_summary")
    suspend fun deleteAll()

    @Query("DELETE FROM launch_summary WHERE launch_type = :launchType")
    suspend fun deleteByLaunchType(launchType: String)

    @Query("SELECT * FROM launch_summary WHERE id = :id")
    suspend fun getById(id: String): LaunchSummaryEntity?

    @Query("SELECT COUNT(*) FROM launch_summary")
    suspend fun getTotalEntries(): Int

    @Query("SELECT COUNT(*) FROM launch_summary WHERE launch_type = :launchType")
    suspend fun getTotalEntriesByType(launchType: String): Int
}
