package com.seancoyle.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.seancoyle.core.common.crashlytics.printLogDebug
import com.seancoyle.core.domain.Order
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchRemoteKeyEntity
import com.seancoyle.database.entities.LaunchStatusEntity

const val REMOTE_KEY_ID = "launches"

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

    @Query("SELECT * FROM launch ORDER BY net ASC")
    fun pagingSource(): PagingSource<Int, LaunchEntity>

    @Upsert
    suspend fun upsert(launch: LaunchEntity): Long

    @Upsert
    suspend fun upsertAll(launches: List<LaunchEntity>): LongArray

    @Query("DELETE FROM launch WHERE id = :id")
    suspend fun deleteById(id: String): Int

    @Query("DELETE FROM launch")
    suspend fun deleteAll()

    @Query("SELECT * FROM launch WHERE id = :id")
    suspend fun getById(id: String): LaunchEntity?

    @Query("SELECT COUNT(*) FROM launch")
    suspend fun getTotalEntries(): Int




    @Query(
        """
        SELECT * FROM launch
        WHERE launch_status = :launchFilter
        ORDER BY net DESC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByLaunchStatusDESC(
        launchFilter: LaunchStatusEntity,
        offset: Int,
        pageSize: Int
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        WHERE launch_status = :launchFilter
        ORDER BY net ASC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByLaunchStatusASC(
        launchFilter: LaunchStatusEntity,
        offset: Int,
        pageSize: Int
    ): List<LaunchEntity>


    @Query(
        """
        SELECT * FROM launch 
        WHERE launch_year = :year
        AND launch_status = :launchFilter
        ORDER BY net DESC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByLaunchStatusAndYearDESC(
        year: String,
        launchFilter: LaunchStatusEntity,
        offset: Int,
        pageSize: Int
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launch_year = :year
        AND launch_status = :launchFilter
        ORDER BY net ASC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByLaunchStatusAndYearASC(
        year: String,
        launchFilter: LaunchStatusEntity,
        offset: Int,
        pageSize: Int
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launch_year = :year
        ORDER BY net DESC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByYearDESC(
        year: String,
        offset: Int,
        pageSize: Int
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launch_year = :year
        ORDER BY net ASC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByYearASC(
        year: String,
        offset: Int,
        pageSize: Int
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY net DESC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun getAllDESC(
        offset: Int,
        pageSize: Int
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY net ASC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun getAllASC(
        offset: Int,
        pageSize: Int
    ): List<LaunchEntity>

}

suspend fun LaunchDao.paginateLaunches(
    launchYear: String,
    order: Order,
    launchStatus: LaunchStatusEntity,
    page: Int,
    pageSize: Int
): List<LaunchEntity> {
    val hasYear = launchYear.isNotEmpty()
    val isOrderDesc = order == Order.DESC
    val offset = page.minus(1).times(pageSize)
    val noFilter = launchStatus == LaunchStatusEntity.ALL

    return when {

        hasYear && isOrderDesc && noFilter -> {
            printLogDebug("DAO", "filterByYearDESC")
            filterByYearDESC(
                year = launchYear,
                offset = offset,
                pageSize = pageSize
            )
        }

        hasYear && !isOrderDesc && noFilter -> {
            printLogDebug("DAO", "filterByYearASC")
            filterByYearASC(
                year = launchYear,
                offset = offset,
                pageSize = pageSize
            )
        }

        isOrderDesc && noFilter -> {
            printLogDebug("DAO", "getAllDESC")
            getAllDESC(
                offset = offset,
                pageSize = pageSize
            )
        }

        !isOrderDesc && noFilter -> {
            printLogDebug("DAO", "getAllASC")
            getAllASC(
                offset = offset,
                pageSize = pageSize
            )
        }

        isOrderDesc && !hasYear -> {
            printLogDebug("DAO", "filterByLaunchStatusDESC")
            filterByLaunchStatusDESC(
                launchFilter = launchStatus,
                offset = offset,
                pageSize = pageSize
            )
        }

        !isOrderDesc && !hasYear -> {
            printLogDebug("DAO", "filterByLaunchStatusASC")
            filterByLaunchStatusASC(
                launchFilter = launchStatus,
                offset = offset,
                pageSize = pageSize
            )
        }

        hasYear && !isOrderDesc -> {
            printLogDebug("DAO", "filterByLaunchStatusAndYearASC")
            filterByLaunchStatusAndYearASC(
                year = launchYear,
                launchFilter = launchStatus,
                offset = offset,
                pageSize = pageSize
            )
        }

        hasYear && isOrderDesc -> {
            printLogDebug("DAO", "filterByLaunchStatusAndYearDESC")
            filterByLaunchStatusAndYearDESC(
                year = launchYear,
                launchFilter = launchStatus,
                offset = offset,
                pageSize = pageSize
            )
        }

        else ->
            emptyList()
    }
}