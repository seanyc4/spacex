package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.CacheErrors
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.cache.launch.FORCE_SEARCH_LAUNCH_EXCEPTION
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.business.interactors.launch.FilterLaunchItemsInCache.Companion.SEARCH_LAUNCH_NO_MATCHING_RESULTS
import com.seancoyle.spacex.business.interactors.launch.FilterLaunchItemsInCache.Companion.SEARCH_LAUNCH_SUCCESS
import com.seancoyle.spacex.di.LaunchDependencies
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_ASC
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_DESC
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_ALL
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_FAILED
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_SUCCESS
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_UNKNOWN
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random

@InternalCoroutinesApi
class FilterLaunchItemsInCacheTest {

    private lateinit var filterLaunchItems: FilterLaunchItemsInCache
    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    lateinit var validLaunchYears: List<String>

    @BeforeEach
    fun init() {
        launchDependencies.build()
        validLaunchYears = launchDependencies.launchDataFactory.provideValidFilterYearDates()
        cacheDataSource = launchDependencies.launchCacheDataSource
        filterLaunchItems = FilterLaunchItemsInCache(
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun `Order All Launch Items By Date Ascending - success`() = runBlocking {

        var launchList = emptyList<LaunchModel>()

        filterLaunchItems.execute(
            year = "",
            order = LAUNCH_ORDER_ASC,
            launchFilter = LAUNCH_ALL,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                SEARCH_LAUNCH_SUCCESS
            )

            value?.data?.launchList?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        checkDateOrderAscending(launchList)
    }

    @Test
    fun `Order All Launch Items By Date Descending - success`() = runBlocking {

        var launchList = emptyList<LaunchModel>()

        filterLaunchItems.execute(
            year = "",
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_ALL,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                SEARCH_LAUNCH_SUCCESS
            )

            value?.data?.launchList?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        checkDateOrderDescending(launchList)
    }

    @Test
    fun `filter launch items by year and date order ASC - success`() = runBlocking {

        var launchList = emptyList<LaunchModel>()
        val launchYear = validLaunchYears[Random.nextInt(validLaunchYears.size)]

        filterLaunchItems.execute(
            year = launchYear,
            order = LAUNCH_ORDER_ASC,
            launchFilter = null,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                SEARCH_LAUNCH_SUCCESS
            )

            value?.data?.launchList?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        assertTrue { launchList.all { it.launchYear == launchYear } }
        checkDateOrderAscending(launchList)
    }

    @Test
    fun `Filter Launch Items By Year - No Results Found`() = runBlocking {

        var launchList = emptyList<LaunchModel>()
        val launchYear = "1000"

        filterLaunchItems.execute(
            year = launchYear,
            order = LAUNCH_ORDER_ASC,
            launchFilter = null,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                SEARCH_LAUNCH_NO_MATCHING_RESULTS
            )

            value?.data?.launchList?.let {
                launchList = it
            }
        }

        // No items should be returned with a launchYear of 1000
        assertTrue { launchList.isEmpty() }
    }

    @Test
    fun `Filter Launch Items By Launch Status LAUNCH_SUCCESS - success`() = runBlocking {

        var launchList = emptyList<LaunchModel>()

        filterLaunchItems.execute(
            year = "",
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                SEARCH_LAUNCH_SUCCESS
            )

            value?.data?.launchList?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_SUCCESS } }
    }

    @Test
    fun `Filter Launch Items By Launch Status LAUNCH_FAILED - success`() = runBlocking {

        var launchList = emptyList<LaunchModel>()

        filterLaunchItems.execute(
            year = "",
            order = LAUNCH_ORDER_ASC,
            launchFilter = LAUNCH_FAILED,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                SEARCH_LAUNCH_SUCCESS
            )

            value?.data?.launchList?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        checkDateOrderAscending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_FAILED } }
    }

    @Test
    fun `Filter Launch Items By Launch Status ALL - success`() = runBlocking {

        var launchList = emptyList<LaunchModel>()
        val allLaunchItems = cacheDataSource.getAll()

        filterLaunchItems.execute(
            year = "",
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_ALL,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                SEARCH_LAUNCH_SUCCESS
            )

            value?.data?.launchList?.let {
                launchList = it
            }
        }

        // Check allLaunchItems is not null
        assertTrue(allLaunchItems != null)

        checkDateOrderDescending(launchList)

        // Check allLaunchItems matches filteredLaunchItems
        assertTrue { launchList.containsAll(allLaunchItems!!) }

    }

    @Test
    fun `Filter Launch Items By Launch Status UNKOWN - success`() = runBlocking {

        var launchList = emptyList<LaunchModel>()

        filterLaunchItems.execute(
            year = "",
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_UNKNOWN,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                SEARCH_LAUNCH_SUCCESS
            )

            value?.data?.launchList?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_UNKNOWN } }

    }

    @Test
    fun `Filter Launch Items By isLaunchSuccess - no results found`() = runBlocking {
        // Year set to 2006 as there were only launch failures that year
        val year = "2006"
        var launchList = emptyList<LaunchModel>()

        filterLaunchItems.execute(
            year = year,
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                SEARCH_LAUNCH_NO_MATCHING_RESULTS
            )

            value?.data?.launchList?.let {
                launchList = it
            }
        }

        // No items should be returned with a launchYear of 2006 and LAUNCH_SUCCESS
        assertTrue { launchList.isEmpty() }
    }

    @Test
    fun `Filter Launch Items Fail - no results found`() = runBlocking {

        var launchList = emptyList<LaunchModel>()

        filterLaunchItems.execute(
            year = FORCE_SEARCH_LAUNCH_EXCEPTION,
            order = LAUNCH_ORDER_DESC,
            launchFilter = null,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assert(
                value?.stateMessage?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
            )

            value?.data?.launchList?.let {
                launchList = it
            }
        }

        // confirm nothing was retrieved
        assertTrue { launchList.isEmpty() }

        // confirm there are launch items in the cache
        val launchItemsInCache = cacheDataSource.getAll()
        if (launchItemsInCache != null) {
            assertTrue { launchItemsInCache.isNotEmpty() }
        }
    }

    private fun checkDateOrderAscending(launchList: List<LaunchModel>) {
        // Check the list and verify the date is less than the next index
        for (item in 0..launchList.size.minus(2)) {
            assertTrue(
                launchList[item].launchDateLocalDateTime <=
                        launchList[item.plus(1)].launchDateLocalDateTime
            )
        }
    }

    private fun checkDateOrderDescending(launchList: List<LaunchModel>) {
        // Check the list and verify the date is greater than the next index
        for (item in 0..launchList.size.minus(2)) {
            assertTrue(
                launchList[item].launchDateLocalDateTime >=
                        launchList[item.plus(1)].launchDateLocalDateTime
            )
        }
    }

}







