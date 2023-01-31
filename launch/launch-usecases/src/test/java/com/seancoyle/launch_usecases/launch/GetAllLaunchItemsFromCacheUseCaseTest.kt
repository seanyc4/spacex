package com.seancoyle.launch_usecases.launch

import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch_datasource.cache.LaunchCacheDataSource
import com.seancoyle.launch_datasource_test.LaunchDependencies
import com.seancoyle.launch_models.model.launch.LaunchModel
import com.seancoyle.launch_usecases.launch.GetAllLaunchItemsFromCacheUseCase.Companion.GET_ALL_LAUNCH_ITEMS_SUCCESS
import com.seancoyle.launch_viewstate.LaunchStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllLaunchItemsFromCacheUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // system in test
    private lateinit var getAllLaunchItems: GetAllLaunchItemsFromCacheUseCase

    // dependencies
    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource

    @BeforeEach
    fun init() {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        getAllLaunchItems = GetAllLaunchItemsFromCacheUseCase(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            cacheDataSource = cacheDataSource
        )
    }


    @Test
    fun `Get All Launch Items From Cache - success - confirm correct`() = runBlocking {

        val numTotal = cacheDataSource.getTotalEntries()
        var results = emptyList<LaunchModel>()

        getAllLaunchItems(
            stateEvent = LaunchStateEvent.GetAllLaunchItemsFromCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_ALL_LAUNCH_ITEMS_SUCCESS
            )

            value?.data?.launchList?.let { list ->
                results = list
            }
        }

        // confirm launch items were retrieved
        assertTrue { results.isNotEmpty() }

        // confirm launch items retrieved matches total number
        assertTrue { results.size == numTotal }
    }

    @Test
    fun `Check All Launch Items Have A Valid Patch Image URL`() = runBlocking {

        val launchList = cacheDataSource.getAll()
        assertTrue { !launchList.isNullOrEmpty() }
        assertTrue(launchList?.all { it.links.missionImage.isNotEmpty() } == true )
    }

}
















