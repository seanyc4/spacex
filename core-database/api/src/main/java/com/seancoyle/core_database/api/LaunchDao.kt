package com.seancoyle.core_database.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch.api.LaunchConstants.ORDER_DESC
import com.seancoyle.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.launch.api.domain.model.LaunchStatus

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

    @Query(
        """
        SELECT * 
        FROM launch 
        WHERE id = :id
        ORDER BY launchDateLocalDateTime DESC
    """
    )
    suspend fun getById(id: String): LaunchEntity?

    @Query(
        """
        SELECT * 
        FROM launch
        ORDER BY launchDateLocalDateTime DESC
    """
    )
    fun getAll(): List<LaunchEntity>?

    @Query("SELECT COUNT(*) FROM launch")
    suspend fun getTotalEntries(): Int?

    @Query(
        """
        SELECT * FROM launch
        WHERE launchStatus = :launchFilter
        ORDER BY launchDateLocalDateTime DESC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun filterByLaunchStatusDESC(
        launchFilter: LaunchStatus,
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        WHERE launchStatus = :launchFilter
        ORDER BY launchDateLocalDateTime ASC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun filterByLaunchStatusASC(
        launchFilter: LaunchStatus,
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>


    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND launchStatus = :launchFilter
        ORDER BY launchDateLocalDateTime DESC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun filterByLaunchStatusAndYearDESC(
        year: String?,
        launchFilter: LaunchStatus,
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND launchStatus = :launchFilter
        ORDER BY launchDateLocalDateTime ASC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun filterByLaunchStatusAndYearASC(
        year: String?,
        launchFilter: LaunchStatus,
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        ORDER BY launchDateLocalDateTime DESC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun filterByYearDESC(
        year: String?,
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        ORDER BY launchDateLocalDateTime ASC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun filterByYearASC(
        year: String?,
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY launchDateLocalDateTime DESC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun getAllDESC(
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY launchDateLocalDateTime ASC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun getAllASC(
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

}

suspend fun LaunchDao.returnOrderedQuery(
    year: String?,
    order: String,
    launchFilter: LaunchStatus,
    page: Int? = 1
): List<LaunchEntity>? {
    val hasYear = !year.isNullOrEmpty()
    val isOrderDesc = order.contains(ORDER_DESC)
    val offset = (page?.minus(1))?.times(PAGINATION_PAGE_SIZE)
    val noFilter = launchFilter == LaunchStatus.ALL

    return when {

        hasYear && isOrderDesc && noFilter -> {
            printLogDebug("DAO", "filterByYearDESC")
            filterByYearDESC(
                year = year,
                page = offset
            )
        }

        hasYear && !isOrderDesc && noFilter -> {
            printLogDebug("DAO", "filterByYearASC")
            filterByYearASC(
                year = year,
                page = offset
            )
        }

        isOrderDesc && noFilter -> {
            printLogDebug("DAO", "getAllDESC")
            getAllDESC(
                page = offset
            )
        }

        !isOrderDesc && noFilter -> {
            printLogDebug("DAO", "getAllASC")
            getAllASC(
                page = offset
            )
        }

        isOrderDesc && !hasYear -> {
            printLogDebug("DAO", "filterByLaunchStatusDESC")
            filterByLaunchStatusDESC(
                launchFilter = launchFilter,
                page = offset
            )
        }

        !isOrderDesc && !hasYear -> {
            printLogDebug("DAO", "filterByLaunchStatusASC")
            filterByLaunchStatusASC(
                launchFilter = launchFilter,
                page = offset
            )
        }

        hasYear && !isOrderDesc -> {
            printLogDebug("DAO", "filterByLaunchStatusAndYearASC")
            filterByLaunchStatusAndYearASC(
                year = year,
                launchFilter = launchFilter,
                page = offset
            )
        }

        hasYear && isOrderDesc -> {
            printLogDebug("DAO", "filterByLaunchStatusAndYearDESC")
            filterByLaunchStatusAndYearDESC(
                year = year,
                launchFilter = launchFilter,
                page = offset
            )
        }

        else ->
            getAll()
    }
}