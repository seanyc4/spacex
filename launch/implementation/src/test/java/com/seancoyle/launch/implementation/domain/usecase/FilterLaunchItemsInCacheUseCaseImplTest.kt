package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_ASC
import com.seancoyle.constants.LaunchDaoConstants.LAUNCH_ORDER_DESC
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_ALL
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_FAILED
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_SUCCESS
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_UNKNOWN
import com.seancoyle.core.data.cache.CacheErrors
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_NO_MATCHING_RESULTS
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_SUCCESS
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.domain.model.ViewModel
import com.seancoyle.launch.api.domain.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.implementation.data.cache.FORCE_SEARCH_LAUNCH_EXCEPTION
import com.seancoyle.launch.implementation.domain.FilterLaunchItemsInCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.LaunchDependencies
import com.seancoyle.launch.implementation.presentation.LaunchEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class FilterLaunchItemsInCacheUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    lateinit var validLaunchYears: List<String>
    private lateinit var underTest: FilterLaunchItemsInCacheUseCase

    @BeforeEach
    fun init() {
        launchDependencies.build()
        validLaunchYears = launchDependencies.launchDataFactory.provideValidFilterYearDates()
        cacheDataSource = launchDependencies.launchCacheDataSource
        underTest =
            FilterLaunchItemsInCacheUseCaseImpl(
                ioDispatcher = mainCoroutineRule.testDispatcher,
                cacheDataSource = cacheDataSource
            )
    }

    @Test
    fun `Order All Launch Items By Date Ascending - success`() = runBlocking {

        var launchList = emptyList<ViewModel>()

        underTest(
            year = "",
            order = LAUNCH_ORDER_ASC,
            launchFilter = LAUNCH_ALL,
            page = 1,
            event = LaunchEvent.FilterLaunchItemsInCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvent.FilterLaunchItemsInCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        checkDateOrderAscending(launchList)
    }

    @Test
    fun `Order All Launch Items By Date Descending - success`() = runBlocking {

        var launchList = emptyList<ViewModel>()

        underTest(
            year = "",
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_ALL,
            page = 1,
            event = LaunchEvent.FilterLaunchItemsInCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvent.FilterLaunchItemsInCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        checkDateOrderDescending(launchList)
    }

    @Test
    fun `filter launch items by year and date order ASC - success`() = runBlocking {

        var launchList = emptyList<ViewModel>()
        val launchYear = validLaunchYears[Random.nextInt(validLaunchYears.size)]

        underTest(
            year = launchYear,
            order = LAUNCH_ORDER_ASC,
            launchFilter = null,
            page = 1,
            event = LaunchEvent.FilterLaunchItemsInCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvent.FilterLaunchItemsInCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        assertTrue { launchList.all { it.launchYear == launchYear } }
        checkDateOrderAscending(launchList)
    }

    @Test
    fun `Filter Launch Items By Year - No Results Found`() = runBlocking {

        var launchList = emptyList<ViewModel>()
        val launchYear = "1000"

        underTest(
            year = launchYear,
            order = LAUNCH_ORDER_ASC,
            launchFilter = null,
            page = 1,
            event = LaunchEvent.FilterLaunchItemsInCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvent.FilterLaunchItemsInCacheEvent.eventName() + EVENT_CACHE_NO_MATCHING_RESULTS
            )

            value?.data?.launches?.let {
                launchList = it
            }
        }

        assertTrue { launchList.isEmpty() }
    }

    @Test
    fun `Filter Launch Items By Launch Status LAUNCH_SUCCESS - success`() = runBlocking {

        var launchList = emptyList<ViewModel>()

        underTest(
            year = "",
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = 1,
            event = LaunchEvent.FilterLaunchItemsInCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvent.FilterLaunchItemsInCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        checkDateOrderDescending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_SUCCESS } }
    }

    @Test
    fun `Filter Launch Items By Launch Status LAUNCH_FAILED - success`() = runBlocking {

        var launchList = emptyList<ViewModel>()

        underTest(
            year = "",
            order = LAUNCH_ORDER_ASC,
            launchFilter = LAUNCH_FAILED,
            page = 1,
            event = LaunchEvent.FilterLaunchItemsInCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvent.FilterLaunchItemsInCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        checkDateOrderAscending(launchList)
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_FAILED } }
    }

    @Test
    fun `Filter Launch Items By Launch Status ALL - success`() = runBlocking {

        var launchList = emptyList<ViewModel>()
        val allLaunchItems = cacheDataSource.getAll()

        underTest(
            year = "",
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_ALL,
            page = 1,
            event = LaunchEvent.FilterLaunchItemsInCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvent.FilterLaunchItemsInCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                launchList = it
            }
        }

        assertTrue(allLaunchItems != null)
        checkDateOrderDescending(launchList)
        assertTrue { launchList.containsAll(allLaunchItems!!) }
    }

    @Test
    fun `Filter Launch Items By Launch Status UNKOWN - success`() = runBlocking {

        var launchList = emptyList<ViewModel>()

        underTest(
            year = "",
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_UNKNOWN,
            page = 1,
            event = LaunchEvent.FilterLaunchItemsInCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvent.FilterLaunchItemsInCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )

            value?.data?.launches?.let {
                launchList = it
            }
        }

        assertTrue(launchList.isNotEmpty())
        assertTrue { launchList.all { it.isLaunchSuccess == LAUNCH_UNKNOWN } }
    }

    @Test
    fun `Filter Launch Items By isLaunchSuccess - no results found`() = runBlocking {

        val year = "2006"
        var launchList = emptyList<ViewModel>()

        underTest(
            year = year,
            order = LAUNCH_ORDER_DESC,
            launchFilter = LAUNCH_SUCCESS,
            page = 1,
            event = LaunchEvent.FilterLaunchItemsInCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvent.FilterLaunchItemsInCacheEvent.eventName() + EVENT_CACHE_NO_MATCHING_RESULTS
            )

            value?.data?.launches?.let {
                launchList = it
            }
        }

        assertTrue { launchList.isEmpty() }
    }

    @Test
    fun `Filter Launch Items Fail - no results found`() = runBlocking {

        var launchList = emptyList<ViewModel>()

        underTest(
            year = FORCE_SEARCH_LAUNCH_EXCEPTION,
            order = LAUNCH_ORDER_DESC,
            launchFilter = null,
            page = 1,
            event = LaunchEvent.FilterLaunchItemsInCacheEvent
        ).collect { value ->
            assert(
                value?.stateMessage?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
            )

            value?.data?.launches?.let {
                launchList = it
            }
        }

        assertTrue { launchList.isEmpty() }

        val launchItemsInCache = cacheDataSource.getAll()
        if (launchItemsInCache != null) {
            assertTrue { launchItemsInCache.isNotEmpty() }
        }
    }

    private fun checkDateOrderAscending(launchList: List<ViewModel>) {
        // Check the list and verify the date is less than the next index
        for (item in 0..launchList.size.minus(2)) {
            assertTrue(
                launchList[item].launchDateLocalDateTime <=
                        launchList[item.plus(1)].launchDateLocalDateTime
            )
        }
    }

    private fun checkDateOrderDescending(launchList: List<ViewModel>) {
        // Check the list and verify the date is greater than the next index
        for (item in 0..launchList.size.minus(2)) {
            assertTrue(
                launchList[item].launchDateLocalDateTime >=
                        launchList[item.plus(1)].launchDateLocalDateTime
            )
        }
    }
}







