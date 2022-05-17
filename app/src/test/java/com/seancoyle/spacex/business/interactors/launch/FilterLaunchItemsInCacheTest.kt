package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.CacheErrors
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.cache.launch.FORCE_SEARCH_LAUNCH_EXCEPTION
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.business.interactors.launch.FilterLaunchItemsInCache.Companion.SEARCH_LAUNCH_NO_MATCHING_RESULTS
import com.seancoyle.spacex.business.interactors.launch.FilterLaunchItemsInCache.Companion.SEARCH_LAUNCH_SUCCESS
import com.seancoyle.spacex.di.DependencyContainer
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_ASC
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

@InternalCoroutinesApi
class FilterLaunchItemsInCacheTest {

    private lateinit var filterLaunchItems: FilterLaunchItemsInCache
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private lateinit var cacheDataSource: LaunchCacheDataSource

    @BeforeEach
    fun init() {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.launchCacheDataSource
        filterLaunchItems = FilterLaunchItemsInCache(
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun filterLaunchItemsByYear_success() = runBlocking {

        var launchList: List<LaunchModel>? = null
        val launchYear = "2020"

        filterLaunchItems.execute(
            year = launchYear,
            order = LAUNCH_ORDER_ASC,
            isLaunchSuccess = null,
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

        // Check all items retrieved have a launch year of 2020"
        assertTrue { launchList?.all { it.launchYear == launchYear } == true }
    }

    @Test
    fun filterLaunchItemsByYear_noResultsFound() = runBlocking {

        var launchList: List<LaunchModel>? = null
        val launchYear = "1000"

        filterLaunchItems.execute(
            year = launchYear,
            order = LAUNCH_ORDER_ASC,
            isLaunchSuccess = null,
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
        assertTrue { launchList?.isEmpty() == true }
    }

    @Test
    fun filterLaunchItemsBy_isLaunchSuccess_launchSuccess() = runBlocking {

        var launchList: List<LaunchModel>? = null

        filterLaunchItems.execute(
            year = "",
            order = LAUNCH_ORDER_ASC,
            isLaunchSuccess = LAUNCH_SUCCESS,
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

        // Check all items retrieved were successfully launched
        assertTrue { launchList?.all { it.isLaunchSuccess == LAUNCH_SUCCESS } == true }
    }

    @Test
    fun filterLaunchItemsBy_isLaunchSuccess_launchFailed() = runBlocking {

        var launchList: List<LaunchModel>? = null

        filterLaunchItems.execute(
            year = "",
            order = LAUNCH_ORDER_ASC,
            isLaunchSuccess = LAUNCH_FAILED,
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

        // Check all items retrieved were successfully launched
        assertTrue { launchList?.all { it.isLaunchSuccess == LAUNCH_FAILED } == true }
    }

    @Test
    fun filterLaunchItemsBy_all() = runBlocking {

        var filteredLaunchItems: List<LaunchModel>? = null
        val allLaunchItems = cacheDataSource.getAll()

        filterLaunchItems.execute(
            year = "",
            order = LAUNCH_ORDER_ASC,
            isLaunchSuccess = LAUNCH_ALL,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                SEARCH_LAUNCH_SUCCESS
            )

            value?.data?.launchList?.let {
                filteredLaunchItems = it
            }
        }

        // Check allLaunchItems is not null
        assertTrue(allLaunchItems != null)

        // Check allLaunchItems matches filteredLaunchItems
        assertTrue { filteredLaunchItems?.containsAll(allLaunchItems!!) == true }

    }

    @Test
    fun filterLaunchItemsBy_unknown() = runBlocking {

        var filteredLaunchItems: List<LaunchModel>? = null

        filterLaunchItems.execute(
            year = "",
            order = LAUNCH_ORDER_ASC,
            isLaunchSuccess = LAUNCH_UNKNOWN,
            page = 1,
            stateEvent = LaunchStateEvent.FilterLaunchItemsInCacheEvent()
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                SEARCH_LAUNCH_SUCCESS
            )

            value?.data?.launchList?.let {
                filteredLaunchItems = it
            }
        }

        // Check allLaunchItems matches filteredLaunchItems
        assertTrue { filteredLaunchItems?.all { it.isLaunchSuccess == LAUNCH_UNKNOWN } == true }

    }

    @Test
    fun filterLaunchItemsBy_isLaunchSuccess_noResultsFound() = runBlocking {

        var launchList: List<LaunchModel>? = null

        filterLaunchItems.execute(
            year = "2006",
            order = LAUNCH_ORDER_ASC,
            isLaunchSuccess = LAUNCH_SUCCESS,
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
        assertTrue { launchList?.isEmpty() == true }
    }

    @Test
    fun filterLaunchItemsFail_noResultsFound() = runBlocking {

        var launchList: List<LaunchModel>? = null

        filterLaunchItems.execute(
            year = FORCE_SEARCH_LAUNCH_EXCEPTION,
            order = LAUNCH_ORDER_ASC,
            isLaunchSuccess = null,
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
        assertTrue { launchList == null }

        // confirm there are launch items in the cache
        val notesInCache = cacheDataSource.getAll()
        if (notesInCache != null) {
            assertTrue { notesInCache.isNotEmpty() }
        }
    }

}







