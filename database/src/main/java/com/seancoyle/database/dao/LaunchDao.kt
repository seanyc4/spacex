package com.seancoyle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seancoyle.core.common.crashlytics.printLogDebug
import com.seancoyle.core.domain.Order
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus

@Dao
interface LaunchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(launch: LaunchEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(launches: List<LaunchEntity>): LongArray

    @Query("DELETE FROM launch WHERE id = :id")
    suspend fun deleteById(id: String): Int

    @Query("DELETE FROM launch WHERE id IN (:ids)")
    suspend fun deleteList(ids: List<String>): Int

    @Query("DELETE FROM launch")
    suspend fun deleteAll()

    @Query("SELECT * FROM launch WHERE id = :id")
    suspend fun getById(id: String): LaunchEntity?

    @Query(
        """
        SELECT * 
        FROM launch
        ORDER BY launchDateLocalDateTime DESC
    """
    )
    fun getAll(): List<LaunchEntity>

    @Query("SELECT COUNT(*) FROM launch")
    suspend fun getTotalEntries(): Int

    @Query(
        """
        SELECT * FROM launch
        WHERE launchStatus = :launchFilter
        ORDER BY launchDateLocalDateTime DESC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByLaunchStatusDESC(
        launchFilter: LaunchStatus,
        offset: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        WHERE launchStatus = :launchFilter
        ORDER BY launchDateLocalDateTime ASC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByLaunchStatusASC(
        launchFilter: LaunchStatus,
        offset: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>


    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND launchStatus = :launchFilter
        ORDER BY launchDateLocalDateTime DESC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByLaunchStatusAndYearDESC(
        year: String,
        launchFilter: LaunchStatus,
        offset: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND launchStatus = :launchFilter
        ORDER BY launchDateLocalDateTime ASC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByLaunchStatusAndYearASC(
        year: String,
        launchFilter: LaunchStatus,
        offset: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        ORDER BY launchDateLocalDateTime DESC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByYearDESC(
        year: String,
        offset: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        ORDER BY launchDateLocalDateTime ASC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun filterByYearASC(
        year: String,
        offset: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY launchDateLocalDateTime DESC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun getAllDESC(
        offset: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY launchDateLocalDateTime ASC 
        LIMIT :pageSize
        OFFSET :offset
        """
    )
    suspend fun getAllASC(
        offset: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

}

suspend fun LaunchDao.paginateLaunches(
    launchYear: String,
    order: Order,
    launchStatus: LaunchStatus,
    page: Int
): List<LaunchEntity> {
    val hasYear = launchYear.isNotEmpty()
    val isOrderDesc = order == Order.DESC
    val offset = page.minus(1).times(PAGINATION_PAGE_SIZE)
    val noFilter = launchStatus == LaunchStatus.ALL

    return when {

        hasYear && isOrderDesc && noFilter -> {
            printLogDebug("DAO", "filterByYearDESC")
            filterByYearDESC(
                year = launchYear,
                offset = offset
            )
        }

        hasYear && !isOrderDesc && noFilter -> {
            printLogDebug("DAO", "filterByYearASC")
            filterByYearASC(
                year = launchYear,
                offset = offset
            )
        }

        isOrderDesc && noFilter -> {
            printLogDebug("DAO", "getAllDESC")
            getAllDESC(
                offset = offset
            )
        }

        !isOrderDesc && noFilter -> {
            printLogDebug("DAO", "getAllASC")
            getAllASC(
                offset = offset
            )
        }

        isOrderDesc && !hasYear -> {
            printLogDebug("DAO", "filterByLaunchStatusDESC")
            filterByLaunchStatusDESC(
                launchFilter = launchStatus,
                offset = offset
            )
        }

        !isOrderDesc && !hasYear -> {
            printLogDebug("DAO", "filterByLaunchStatusASC")
            filterByLaunchStatusASC(
                launchFilter = launchStatus,
                offset = offset
            )
        }

        hasYear && !isOrderDesc -> {
            printLogDebug("DAO", "filterByLaunchStatusAndYearASC")
            filterByLaunchStatusAndYearASC(
                year = launchYear,
                launchFilter = launchStatus,
                offset = offset
            )
        }

        hasYear && isOrderDesc -> {
            printLogDebug("DAO", "filterByLaunchStatusAndYearDESC")
            filterByLaunchStatusAndYearDESC(
                year = launchYear,
                launchFilter = launchStatus,
                offset = offset
            )
        }

        else ->
            getAll()
    }
}