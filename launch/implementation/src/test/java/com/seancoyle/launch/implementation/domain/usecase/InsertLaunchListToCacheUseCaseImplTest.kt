package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.cache.CacheErrors
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.api.LaunchCacheDataSource
import com.seancoyle.launch.api.model.LaunchFactory
import com.seancoyle.launch.api.usecase.InsertLaunchListToCacheUseCase
import com.seancoyle.launch.implementation.domain.InsertLaunchListToCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.InsertLaunchListToCacheUseCaseImpl.Companion.INSERT_LAUNCH_LIST_SUCCESS
import com.seancoyle.launch.implementation.domain.LaunchDependencies
import com.seancoyle.launch.implementation.presentation.LaunchEvent
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
class InsertLaunchListToCacheUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    private lateinit var factory: LaunchFactory
    private lateinit var underTest: InsertLaunchListToCacheUseCase

    @BeforeEach
    fun setup() {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        factory = launchDependencies.launchFactory
        underTest =
            InsertLaunchListToCacheUseCaseImpl(
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

        underTest(
            launchList = newLaunchList,
            stateEvent = LaunchEvent.InsertLaunchItemsToCacheEvent(
                launchList = newLaunchList
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                INSERT_LAUNCH_LIST_SUCCESS
            )
        }

        val cachedLaunchList = cacheDataSource.getAll()
        assertTrue { cachedLaunchList?.containsAll(newLaunchList) == true }
    }

    @Test
    fun insertLaunchList_fail() = runBlocking {

        val newLaunchList = factory.createLaunchListTest(
            num = 100,
            id = FORCE_GENERAL_FAILURE
        )

        underTest(
            launchList = newLaunchList,
            stateEvent = LaunchEvent.InsertLaunchItemsToCacheEvent(
                launchList = newLaunchList
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                InsertLaunchListToCacheUseCaseImpl.INSERT_LAUNCH_LIST_FAILED
            )
        }

        val cachedData = cacheDataSource.getAll()
        assertTrue { cachedData?.containsAll(newLaunchList) == false }
    }

    @Test
    fun throwException_checkGenericError() = runBlocking {

        val newLaunchList = factory.createLaunchListTest(
            num = 100,
            id = FORCE_NEW_LAUNCH_EXCEPTION
        )

        underTest(
            launchList = newLaunchList,
            stateEvent = LaunchEvent.InsertLaunchItemsToCacheEvent(
                launchList = newLaunchList
            )
        ).collect { value ->
            assert(
                value?.stateMessage?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
            )
        }

        val cachedData = cacheDataSource.getAll()
        assertTrue { cachedData?.containsAll(newLaunchList) == false }
    }
}





















