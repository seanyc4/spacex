package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.business.interactors.launch.GetAllLaunchItemsFromCache.Companion.GET_ALL_LAUNCH_ITEMS_SUCCESS
import com.seancoyle.spacex.di.LaunchDependencies
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/*
Test cases:
1. getAllLaunchItemsFromCache_success_confirmCorrect()
    a) get the total number of launch in cache
    b) query the cache to return all items in the table
    c) listen for GET_ALL_LAUNCH_ITEMS_SUCCESS from flow emission
    d) compare with the total number with the results from the fake data set
*/
@InternalCoroutinesApi
class GetAllLaunchItemsFromCacheTest {

    // system in test
    private lateinit var getAllLaunchItems: GetAllLaunchItemsFromCache

    // dependencies
    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource

    @BeforeEach
    fun init() {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        getAllLaunchItems = GetAllLaunchItemsFromCache(
            cacheDataSource = cacheDataSource
        )
    }


    @Test
    fun `Get All Launch Items From Cache - success - confirm correct`() = runBlocking {

        val numTotal = cacheDataSource.getTotalEntries()
        var results = emptyList<LaunchModel>()

        getAllLaunchItems.execute(
            stateEvent = GetAllLaunchItemsFromCacheEvent
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
















