package com.seancoyle.launch_usecases.launch

import com.seancoyle.core.cache.CacheErrors
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch_datasource.cache.LaunchCacheDataSource
import com.seancoyle.launch_datasource_test.LaunchDependencies
import com.seancoyle.launch_models.model.launch.LaunchFactory
import com.seancoyle.launch_usecases.launch.InsertLaunchListToCacheUseCase.Companion.INSERT_LAUNCH_LIST_SUCCESS
import com.seancoyle.launch_viewstate.LaunchStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

const val FORCE_NEW_LAUNCH_EXCEPTION = -4
const val FORCE_GENERAL_FAILURE = -5
const val FORCE_SEARCH_LAUNCH_EXCEPTION = "FORCE_SEARCH_LAUNCH_EXCEPTION"

@OptIn(ExperimentalCoroutinesApi::class)
class InsertLaunchListToCacheUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // system in test
    private lateinit var insertLaunchListToCacheUseCase: InsertLaunchListToCacheUseCase

    // dependencies
    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    private lateinit var factory: LaunchFactory

    @BeforeEach
    fun setup() {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        factory = launchDependencies.launchFactory
        insertLaunchListToCacheUseCase = InsertLaunchListToCacheUseCase(
            ioDispatcher = mainCoroutineRule.testDispatcher,
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





















