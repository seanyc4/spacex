package com.seancoyle.launch_usecases.launch

import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_datasource_test.LaunchDependencies
import com.seancoyle.launch_models.model.launch.LaunchFactory
import com.seancoyle.launch_models.model.launch.LaunchModel
import com.seancoyle.launch_usecases.launch.GetLaunchItemByIdFromCacheUseCase.Companion.GET_LAUNCH_ITEM_BY_ID_SUCCESS
import com.seancoyle.launch_viewstate.LaunchStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/*
Test cases:
1. getLaunchItemsByIdFromCache_success_confirmCorrect()
    a) get a launch item from the db with a specific id
    b) listen for GET_LAUNCH_ITEM_BY_ID_SUCCESS from flow emission
    c) compare the id to requested with the id of the launch item we retrieved
*/
@OptIn(ExperimentalCoroutinesApi::class)
class GeLaunchItemByIdFromCacheTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // system in test
    private lateinit var getLaunchItemById: GetLaunchItemByIdFromCacheUseCase

    // dependencies
    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    private lateinit var factory: LaunchFactory

    @BeforeEach
    fun setup() {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        factory = launchDependencies.launchFactory
        getLaunchItemById = GetLaunchItemByIdFromCacheUseCase(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun getLaunchItemsByIdFromCache_success_confirmCorrect() = runBlocking {

        val id = 1
        var retrievedLaunch: LaunchModel? = null

        getLaunchItemById(
            id = id,
            stateEvent = LaunchStateEvent.GetLaunchItemFromCacheEvent(
                id = id
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_LAUNCH_ITEM_BY_ID_SUCCESS
            )

            value?.data?.launch?.let { item ->
                retrievedLaunch = item
            }
        }

        assertTrue { retrievedLaunch?.id == id }
    }

}
















