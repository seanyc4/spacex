package com.seancoyle.spacex.framework.datasource.cache.dao.launch

import androidx.room.*
import com.seancoyle.spacex.framework.datasource.cache.model.launch.LaunchEntity

const val LAUNCH_ORDER_ASC: String = "ASC"
const val LAUNCH_ORDER_DESC: String = "DESC"
const val LAUNCH_PAGINATION_PAGE_SIZE = 30

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
        WHERE isLaunchSuccess = :isLaunchSuccess
        ORDER BY launchDateLocalDateTime DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun launchItemsWithSuccessOrderByYearDESC(
        isLaunchSuccess: Int?,
        page: Int,
        pageSize: Int = LAUNCH_PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        WHERE isLaunchSuccess = :isLaunchSuccess
        ORDER BY launchDateLocalDateTime ASC LIMIT (:page * :pageSize)
        """
    )
    suspend fun launchItemsWithSuccessOrderByYearASC(
        isLaunchSuccess: Int?,
        page: Int,
        pageSize: Int = LAUNCH_PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>


    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND isLaunchSuccess = :isLaunchSuccess
        ORDER BY launchDateLocalDateTime DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchLaunchItemsWithSuccessOrderByYearDESC(
        year: String?,
        isLaunchSuccess: Int?,
        page: Int,
        pageSize: Int = LAUNCH_PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND isLaunchSuccess = :isLaunchSuccess
        ORDER BY launchDateLocalDateTime ASC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchLaunchItemsWithSuccessOrderByYearASC(
        year: String?,
        isLaunchSuccess: Int?,
        page: Int,
        pageSize: Int = LAUNCH_PAGINATION_PAGE_SIZE
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
        pageSize: Int = LAUNCH_PAGINATION_PAGE_SIZE
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
        pageSize: Int = LAUNCH_PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY launchDateLocalDateTime DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun launchItemsOrderByYearDESC(
        page: Int,
        pageSize: Int = LAUNCH_PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY launchDateLocalDateTime ASC LIMIT (:page * :pageSize)
        """
    )
    suspend fun launchItemsOrderByYearASC(
        page: Int,
        pageSize: Int = LAUNCH_PAGINATION_PAGE_SIZE
    ): List<LaunchEntity>

}

suspend fun LaunchDao.returnOrderedQuery(
    year: String?,
    order: String,
    launchFilter: Int?,
    page: Int
): List<LaunchEntity>? {

    when {

        launchFilter != null && order.contains(LAUNCH_ORDER_DESC) && year.isNullOrEmpty() -> {
            return launchItemsWithSuccessOrderByYearDESC(
                isLaunchSuccess = launchFilter,
                page = page
            )
        }

        launchFilter != null && order.contains(LAUNCH_ORDER_ASC) && year.isNullOrEmpty() -> {
            return launchItemsWithSuccessOrderByYearASC(
                isLaunchSuccess = launchFilter,
                page = page
            )
        }

        !year.isNullOrEmpty() && order.contains(LAUNCH_ORDER_DESC) && launchFilter == null -> {
            return searchLaunchItemsOrderByYearDESC(
                year = year,
                page = page
            )
        }

        !year.isNullOrEmpty() && order.contains(LAUNCH_ORDER_ASC) && launchFilter == null -> {
            return searchLaunchItemsOrderByYearASC(
                year = year,
                page = page
            )
        }

        !year.isNullOrEmpty() && launchFilter != null && order.contains(LAUNCH_ORDER_ASC) ->
            return searchLaunchItemsWithSuccessOrderByYearASC(
                year = year,
                isLaunchSuccess = launchFilter,
                page = page
            )

        !year.isNullOrEmpty() && launchFilter != null && order.contains(LAUNCH_ORDER_DESC) ->
            return searchLaunchItemsWithSuccessOrderByYearDESC(
                year = year,
                isLaunchSuccess = launchFilter,
                page = page
            )

        order.contains(LAUNCH_ORDER_DESC) -> {
            return launchItemsOrderByYearDESC(
                page = page
            )
        }

        order.contains(LAUNCH_ORDER_ASC) -> {
            return launchItemsOrderByYearASC(
                page = page
            )
        }

        else ->
            return getAll()
    }
}












