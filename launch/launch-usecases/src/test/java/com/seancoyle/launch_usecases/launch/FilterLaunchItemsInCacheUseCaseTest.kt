package com.seancoyle.launch_usecases.launch

import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_ASC
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_DESC
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_FAILED
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_SUCCESS
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_UNKNOWN
import com.seancoyle.core.cache.CacheErrors
import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_models.model.launch.LaunchModel
import com.seancoyle.launch_usecases.launch.FilterLaunchItemsInCacheUseCase.Companion.SEARCH_LAUNCH_NO_MATCHING_RESULTS
import com.seancoyle.launch_usecases.launch.FilterLaunchItemsInCacheUseCase.Companion.SEARCH_LAUNCH_SUCCESS
import com.seancoyle.launch_datasource_test.LaunchDependencies
import com.seancoyle.launch_viewstate.LaunchStateEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random

@InternalCoroutinesApi
class FilterLaunchItemsInCacheUseCaseTest {

    private lateinit var filterLaunchItems: FilterLaunchItemsInCacheUseCase
    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    lateinit var validLaunchYears: List<String>

    @BeforeEach
    fun init() {
        launchDependencies.build()
        validLaunchYears = launchDependencies.launchDataFactory.provideValidFilterYearDates()
        cacheDataSource = launchDependencies.launchCacheDataSource
        filterLaunchItems = FilterLaunchItemsInCacheUseCase(
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun `Order All Launch Items By Date Ascending - success`() = runBlocking {

        var launchList = emptyList<LaunchModel>()

        filterLaunchItems(
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

        filterLaunchItems(
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

        filterLaunchItems(
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

        filterLaunchItems(
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

        filterLaunchItems(
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

        filterLaunchItems(
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

        filterLaunchItems(
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

        filterLaunchItems(
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

        filterLaunchItems(
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

        filterLaunchItems(
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







