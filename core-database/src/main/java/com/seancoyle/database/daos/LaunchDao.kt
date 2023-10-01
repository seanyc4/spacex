package com.seancoyle.database.daos

import androidx.room.*
import com.seancoyle.core.Constants.ORDER_DESC
import com.seancoyle.core.Constants.PAGINATION_PAGE_SIZE
import com.seancoyle.database.entities.LaunchEntity
import kotlinx.coroutines.flow.Flow


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
    fun getAll(): Flow<List<LaunchEntity>>

    @Query("SELECT COUNT(*) FROM launch")
    suspend fun getTotalEntries(): Int

    @Query(
        """
        SELECT * FROM launch
        WHERE isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime DESC LIMIT (:page * :pageSize)
        """
    )
    fun launchItemsWithSuccessOrderByYearDESC(
        launchFilter: Int?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): Flow<List<LaunchEntity>>

    @Query(
        """
        SELECT * FROM launch
        WHERE isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime ASC LIMIT (:page * :pageSize)
        """
    )
    fun launchItemsWithSuccessOrderByYearASC(
        launchFilter: Int?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): Flow<List<LaunchEntity>>


    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime DESC LIMIT (:page * :pageSize)
        """
    )
    fun searchLaunchItemsWithSuccessOrderByYearDESC(
        year: String?,
        launchFilter: Int?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): Flow<List<LaunchEntity>>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        AND isLaunchSuccess = :launchFilter
        ORDER BY launchDateLocalDateTime ASC LIMIT (:page * :pageSize)
        """
    )
    fun searchLaunchItemsWithSuccessOrderByYearASC(
        year: String?,
        launchFilter: Int?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): Flow<List<LaunchEntity>>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        ORDER BY launchDateLocalDateTime DESC LIMIT (:page * :pageSize)
        """
    )
    fun searchLaunchItemsOrderByYearDESC(
        year: String?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): Flow<List<LaunchEntity>>

    @Query(
        """
        SELECT * FROM launch 
        WHERE launchYear = :year
        ORDER BY launchDateLocalDateTime ASC LIMIT (:page * :pageSize)
        """
    )
    fun searchLaunchItemsOrderByYearASC(
        year: String?,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): Flow<List<LaunchEntity>>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY launchDateLocalDateTime DESC LIMIT (:page * :pageSize)
        """
    )
    fun launchItemsOrderByYearDESC(
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): Flow<List<LaunchEntity>>

    @Query(
        """
        SELECT * FROM launch
        ORDER BY launchDateLocalDateTime ASC LIMIT (:page * :pageSize)
        """
    )
    fun launchItemsOrderByYearASC(
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): Flow<List<LaunchEntity>>

}

fun LaunchDao.returnOrderedQuery(
    year: String?,
    order: String,
    launchFilter: Int?,
    page: Int
): Flow<List<LaunchEntity>> {
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

        else -> getAll()
    }
}