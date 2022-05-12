package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.CacheErrors
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.cache.launch.FORCE_GENERAL_FAILURE
import com.seancoyle.spacex.business.data.cache.launch.FORCE_NEW_LAUNCH_EXCEPTION
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.business.interactors.launch.InsertLaunchListToCache.Companion.INSERT_LAUNCH_LIST_SUCCESS
import com.seancoyle.spacex.di.DependencyContainer
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/*
Test cases:
1. insertLaunchList_success()
    a) insert a new launch list
    b) listen for INSERT_LAUNCH_LIST_SUCCESS emission from flow
    c) confirm cache was updated with new launch list
2. insertLaunchList_fail()
    a) insert a new launch list
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_LAUNCH_LIST_FAILED emission from flow
    e) confirm cache was not updated
3. throwException_checkGenericError()
    a) insert a new launch list
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
 */
@InternalCoroutinesApi
class InsertLaunchListToCacheTest {

    // system in test
    private val insertLaunchListToCache: InsertLaunchListToCache

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: LaunchCacheDataSource
    private val factory: LaunchFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.launchCacheDataSource
        factory = dependencyContainer.launchFactory
        insertLaunchListToCache = InsertLaunchListToCache(
            cacheDataSource = cacheDataSource,
            factory = factory
        )
    }

    @Test
    fun insertLaunchList_success() = runBlocking {

        val newLaunchList = factory.createLaunchListTest(100)

        insertLaunchListToCache.execute(
            launchList = newLaunchList,
            stateEvent = InsertLaunchListToCacheEvent(
               launchList = newLaunchList
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                INSERT_LAUNCH_LIST_SUCCESS
            )
        }

        // confirm cache was updated
        val cachedLaunchList = cacheDataSource.getAll()
        assertTrue { cachedLaunchList == newLaunchList }
    }

    @Test
    fun insertLaunchList_fail() = runBlocking {

        val newLaunchList = factory.createLaunchListTest(
            num = 100,
            id = FORCE_GENERAL_FAILURE
        )

        insertLaunchListToCache.execute(
            launchList = newLaunchList,
            stateEvent = InsertLaunchListToCacheEvent(
                launchList = newLaunchList
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                InsertLaunchListToCache.INSERT_LAUNCH_LIST_FAILED
            )
        }

        // confirm cache was not changed
        val cachedData = cacheDataSource.getAll()
        assertTrue { cachedData == null }
    }

    @Test
    fun throwException_checkGenericError() = runBlocking {

        val newLaunchList = factory.createLaunchListTest(
            num = 100,
            id = FORCE_NEW_LAUNCH_EXCEPTION
        )

        insertLaunchListToCache.execute(
            launchList = newLaunchList,
            stateEvent = InsertLaunchListToCacheEvent(
                launchList = newLaunchList
            )
        ).collect { value ->
            assert(
                value?.stateMessage?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
            )
        }

        // confirm cache was not changed
        val cachedData = cacheDataSource.getAll()
        assertTrue { cachedData == null }
    }
}





















