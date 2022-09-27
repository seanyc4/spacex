package com.seancoyle.launch_usecases.launch

import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_datasource_test.LaunchDependencies
import com.seancoyle.launch_models.model.launch.LaunchFactory
import com.seancoyle.launch_usecases.launch.GetNumLaunchItemsFromCacheUseCase.Companion.GET_NUM_LAUNCH_ITEMS_SUCCESS
import com.seancoyle.launch_viewstate.LaunchStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/*
Test cases:
1. getNumLaunchItems_success_confirmCorrect()
    a) get the number of launch in cache
    b) listen for GET_NUM_LAUNCH_ITEMS_SUCCESS from flow emission
    c) compare with the number of launch in the fake data set
*/
@OptIn(ExperimentalCoroutinesApi::class)
class GetNumLaunchItemsFromCacheUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // system in test
    private lateinit var getNumLaunchItemsFromCacheUseCase: GetNumLaunchItemsFromCacheUseCase

    // dependencies
    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    private lateinit var factory: LaunchFactory

    @BeforeEach
    fun setup() {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        factory = launchDependencies.launchFactory
        getNumLaunchItemsFromCacheUseCase = GetNumLaunchItemsFromCacheUseCase(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun getNumLaunchItems_success_confirmCorrect() = runBlocking {

        var numItems = 0
        getNumLaunchItemsFromCacheUseCase(
            stateEvent = LaunchStateEvent.GetNumLaunchItemsInCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_NUM_LAUNCH_ITEMS_SUCCESS
            )
            numItems = value?.data?.numLaunchItemsInCache ?: 0
        }

        val actualNumItemsInCache = cacheDataSource.getTotalEntries()
        assertTrue { actualNumItemsInCache == numItems }
    }


}
















