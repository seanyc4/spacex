package com.seancoyle.core_database.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch.api.LaunchNetworkConstants.ORDER_DESC
import com.seancoyle.launch.api.LaunchNetworkConstants.PAGINATION_PAGE_SIZE

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
    suspend fun getById(id: String):LaunchEntity?

    @Query(
        """
        SELECT * 
        FROM launch
        ORDER BY launchDateLocalDateTime DESC
    """
    )
    fun getAll(): List<LaunchEntity>?

    @Query("SELECT COUNT(*) FROM launch")
    suspend fun getTotalEntries(): Int

    @Query(
        """
        SELECT * FROM launch
        WHERE isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime DESC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun launchItemsWithSuccessOrderByYearDESC(
        launchFilter: Int?,
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        WHERE isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime ASC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun launchItemsWithSuccessOrderByYearASC(
        launchFilter: Int?,
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>


    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime DESC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun searchLaunchItemsWithSuccessOrderByYearDESC(
        year: String?,
        launchFilter: Int?,
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime ASC 
        LIMIT :pageSize
        OFFSET :page
        """
    )
    suspend fun searchLaunchItemsWithSuccessOrderByYearASC(
        year: String?,
        launchFilter: Int?,
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
    suspend fun searchLaunchItemsOrderByYearDESC(
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
    suspend fun searchLaunchItemsOrderByYearASC(
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
    suspend fun launchItemsOrderByYearDESC(
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
    suspend fun launchItemsOrderByYearASC(
        page: Int? = 1,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

}

suspend fun LaunchDao.returnOrderedQuery(
    year: String?,
    order: String,
    launchFilter: Int?,
    page: Int? = 1
): List<LaunchEntity>? {
    val hasYear = !year.isNullOrEmpty()
    val hasLaunchFilter = launchFilter != null
    val isOrderDesc = order.contains(ORDER_DESC)
    val offset = (page?.minus(1))?.times(PAGINATION_PAGE_SIZE)

    return when {

        hasLaunchFilter && isOrderDesc && !hasYear -> {
            printLogDebug("DAO", "launchItemsWithSuccessOrderByYearDESC")
            launchItemsWithSuccessOrderByYearDESC(
                launchFilter = launchFilter,
                page = offset
            )
        }

        hasLaunchFilter && !isOrderDesc && !hasYear -> {
            printLogDebug("DAO", "launchItemsWithSuccessOrderByYearASC")
            launchItemsWithSuccessOrderByYearASC(
                launchFilter = launchFilter,
                page = offset
            )
        }

        hasYear && isOrderDesc && !hasLaunchFilter -> {
            printLogDebug("DAO", "searchLaunchItemsOrderByYearDESC")
            searchLaunchItemsOrderByYearDESC(
                year = year,
                page = offset
            )
        }

        hasYear && !isOrderDesc && !hasLaunchFilter -> {
            printLogDebug("DAO", "searchLaunchItemsOrderByYearASC")
            searchLaunchItemsOrderByYearASC(
                year = year,
                page = offset
            )
        }

        hasYear && hasLaunchFilter && !isOrderDesc -> {
            printLogDebug("DAO", "searchLaunchItemsWithSuccessOrderByYearASC")
            searchLaunchItemsWithSuccessOrderByYearASC(
                year = year,
                launchFilter = launchFilter,
                page = offset
            )
        }

        hasYear && hasLaunchFilter && isOrderDesc -> {
            printLogDebug("DAO", "searchLaunchItemsWithSuccessOrderByYearDESC")
            searchLaunchItemsWithSuccessOrderByYearDESC(
                year = year,
                launchFilter = launchFilter,
                page = offset
            )
        }


        isOrderDesc -> {
            printLogDebug("DAO", "launchItemsOrderByYearDESC")
            launchItemsOrderByYearDESC(
                page = offset
            )
        }

        !isOrderDesc -> {
            printLogDebug("DAO", "launchItemsOrderByYearASC")
            launchItemsOrderByYearASC(
                page = offset
            )
        }

        else ->
            getAll()
    }
}