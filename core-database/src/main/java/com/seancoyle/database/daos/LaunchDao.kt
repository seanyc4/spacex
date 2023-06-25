package com.seancoyle.database.daos

import androidx.room.*
import com.seancoyle.core.Constants.ORDER_DESC
import com.seancoyle.core.Constants.PAGINATION_PAGE_SIZE
import com.seancoyle.database.entities.LaunchEntity


@Dao
interface LaunchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(launch: LaunchEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(launches: List<LaunchEntity>): LongArray

    @Query("DELETE FROM launch WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    @Query("DELETE FROM launch WHERE id IN (:ids)")
    suspend fun deleteList(ids: List<Int>): Int

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
    suspend fun getById(id: Int): LaunchEntity?

    @Query(
        """
        SELECT * 
        FROM launch
        ORDER BY launchDateLocalDateTime DESC
    """
    )
    suspend fun getAll(): List<LaunchEntity>?

    @Query("SELECT COUNT(*) FROM launch")
    suspend fun getTotalEntries(): Int

    @Query(
        """
        SELECT * FROM launch
        WHERE isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun launchItemsWithSuccessOrderByYearDESC(
        launchFilter: Int?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        WHERE isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime ASC LIMIT (:page * :pageSize)
        """
    )
    suspend fun launchItemsWithSuccessOrderByYearASC(
        launchFilter: Int?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>


    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchLaunchItemsWithSuccessOrderByYearDESC(
        year: String?,
        launchFilter: Int?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime ASC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchLaunchItemsWithSuccessOrderByYearASC(
        year: String?,
        launchFilter: Int?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        ORDER BY launchDateLocalDateTime DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchLaunchItemsOrderByYearDESC(
        year: String?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        ORDER BY launchDateLocalDateTime ASC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchLaunchItemsOrderByYearASC(
        year: String?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY launchDateLocalDateTime DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun launchItemsOrderByYearDESC(
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY launchDateLocalDateTime ASC LIMIT (:page * :pageSize)
        """
    )
    suspend fun launchItemsOrderByYearASC(
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

}

suspend fun LaunchDao.returnOrderedQuery(
    year: String?,
    order: String,
    launchFilter: Int?,
    page: Int
): List<LaunchEntity>? {
    val hasYear = !year.isNullOrEmpty()
    val hasLaunchFilter = launchFilter != null
    val isOrderDesc = order.contains(ORDER_DESC)

    return when {

        hasLaunchFilter && isOrderDesc && !hasYear -> {
            launchItemsWithSuccessOrderByYearDESC(
                launchFilter = launchFilter,
                page = page
            )
        }

        hasLaunchFilter && !isOrderDesc && !hasYear -> {
            launchItemsWithSuccessOrderByYearASC(
                launchFilter = launchFilter,
                page = page
            )
        }

        hasYear && isOrderDesc && !hasLaunchFilter -> {
            searchLaunchItemsOrderByYearDESC(
                year = year,
                page = page
            )
        }

        hasYear && !isOrderDesc && !hasLaunchFilter -> {
            searchLaunchItemsOrderByYearASC(
                year = year,
                page = page
            )
        }

        hasYear && hasLaunchFilter && !isOrderDesc ->
            searchLaunchItemsWithSuccessOrderByYearASC(
                year = year,
                launchFilter = launchFilter,
                page = page
            )

        hasYear && hasLaunchFilter && isOrderDesc ->
            searchLaunchItemsWithSuccessOrderByYearDESC(
                year = year,
                launchFilter = launchFilter,
                page = page
            )

        isOrderDesc -> {
            launchItemsOrderByYearDESC(
                page = page
            )
        }

        !isOrderDesc -> {
            launchItemsOrderByYearASC(
                page = page
            )
        }

        else ->
            getAll()
    }
}