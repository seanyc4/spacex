package com.seancoyle.launch_usecases.launch

import com.seancoyle.core.cache.CacheErrors
import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_datasource_test.LaunchDependencies
import com.seancoyle.launch_models.model.launch.LaunchFactory
import com.seancoyle.launch_usecases.launch.InsertLaunchListToCacheUseCase.Companion.INSERT_LAUNCH_LIST_SUCCESS
import com.seancoyle.launch_viewstate.LaunchStateEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

const val FORCE_DELETE_LAUNCH_EXCEPTION = -2
const val FORCE_DELETES_LAUNCH_EXCEPTION = -3
const val FORCE_NEW_LAUNCH_EXCEPTION = -4
const val FORCE_GENERAL_FAILURE = -5
const val FORCE_SEARCH_LAUNCH_EXCEPTION = "FORCE_SEARCH_LAUNCH_EXCEPTION"

/*
Test cases:
1. insertLaunchList_success()
    a) insert a new launch list
    b) listen for INSERT_LAUNCH_LIST_SUCCESS emission from flow
    c) confirm cache was updated with new launch list
2. insertLaunchList_fail()
    a) insert a new launch list
    b) force a failure (return empty long array from db operation)
    c) listen for INSERT_LAUNCH_LIST_FAILED emission from flow
    e) confirm cache was not updated
3. throwException_checkGenericError()
    a) insert a new launch list
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
 */
@InternalCoroutinesApi
class InsertLaunchListToCacheUseCaseTest {

    // system in test
    private val insertLaunchListToCacheUseCase: InsertLaunchListToCacheUseCase

    // dependencies
    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private val cacheDataSource: LaunchCacheDataSource
    private val factory: LaunchFactory

    init {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        factory = launchDependencies.launchFactory
        insertLaunchListToCacheUseCase = InsertLaunchListToCacheUseCase(
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun insertLaunchList_success() = runBlocking {

        val newLaunchList = factory.createLaunchListTest(
            num = 100,
            id = null
        )

        insertLaunchListToCacheUseCase(
            launchList = newLaunchList,
            stateEvent = LaunchStateEvent.InsertLaunchItemsToCacheEvent(
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
        assertTrue { cachedLaunchList?.containsAll(newLaunchList) == true }
    }

    @Test
    fun insertLaunchList_fail() = runBlocking {

        val newLaunchList = factory.createLaunchListTest(
            num = 100,
            id = FORCE_GENERAL_FAILURE
        )

        insertLaunchListToCacheUseCase(
            launchList = newLaunchList,
            stateEvent = LaunchStateEvent.InsertLaunchItemsToCacheEvent(
                launchList = newLaunchList
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                InsertLaunchListToCacheUseCase.INSERT_LAUNCH_LIST_FAILED
            )
        }

        // confirm cache was not changed
        val cachedData = cacheDataSource.getAll()
        assertTrue { cachedData?.containsAll(newLaunchList) == false }
    }

    @Test
    fun throwException_checkGenericError() = runBlocking {

        val newLaunchList = factory.createLaunchListTest(
            num = 100,
            id = FORCE_NEW_LAUNCH_EXCEPTION
        )

        insertLaunchListToCacheUseCase(
            launchList = newLaunchList,
            stateEvent = LaunchStateEvent.InsertLaunchItemsToCacheEvent(
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
        assertTrue { cachedData?.containsAll(newLaunchList) == false }
    }
}





















