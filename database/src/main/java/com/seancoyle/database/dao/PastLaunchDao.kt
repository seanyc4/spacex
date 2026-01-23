package com.seancoyle.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.seancoyle.database.entities.PastLaunchEntity
import com.seancoyle.database.entities.PastRemoteKeyEntity

@Dao
interface PastLaunchDao {

    @Query("SELECT * FROM past_launches")
    fun pagingSource(): PagingSource<Int, PastLaunchEntity>

    @Upsert
    suspend fun upsert(launch: PastLaunchEntity): Long

    @Upsert
    suspend fun upsertAll(launches: List<PastLaunchEntity>): LongArray

    @Query("DELETE FROM past_launches")
    suspend fun deleteAll()

    @Query("SELECT * FROM past_launches WHERE id = :id")
    suspend fun getById(id: String): PastLaunchEntity?

    @Query("SELECT COUNT(*) FROM past_launches")
    suspend fun getTotalEntries(): Int

    @Transaction
    suspend fun refreshLaunchesWithKeys(
        launches: List<PastLaunchEntity>,
        remoteKeyDao: PastRemoteKeyDao,
        remoteKeys: List<PastRemoteKeyEntity>,
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
    suspend fun refreshLaunches(launches: List<PastLaunchEntity>) {
        deleteAll()
        upsertAll(launches)
    }
}
