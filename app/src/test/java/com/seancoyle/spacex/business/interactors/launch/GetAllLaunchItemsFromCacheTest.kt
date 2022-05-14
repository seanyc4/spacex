package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.interactors.launch.GetAllLaunchItemsFromCache.Companion.GET_ALL_LAUNCH_ITEMS_SUCCESS
import com.seancoyle.spacex.di.DependencyContainer
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
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
    private val getAllLaunchItems: GetAllLaunchItemsFromCache

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val cacheDataSource: LaunchCacheDataSource
    private val factory: LaunchFactory

    init {
        dependencyContainer.build()
        cacheDataSource = dependencyContainer.launchCacheDataSource
        factory = dependencyContainer.launchFactory
        getAllLaunchItems = GetAllLaunchItemsFromCache(
            cacheDataSource = cacheDataSource
        )
    }


    @Test
    fun getAllLaunchItemsFromCache_success_confirmCorrect() = runBlocking {

        val numTotal = cacheDataSource.getTotalEntries()
        var results: ArrayList<LaunchDomainEntity>? = null

        getAllLaunchItems.execute(
            stateEvent = GetAllLaunchDataFromCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                GET_ALL_LAUNCH_ITEMS_SUCCESS
            )

            value?.data?.launchList?.let { list ->
                results = ArrayList(list)
            }
        }

        // confirm launch items were retrieved
        assertTrue { results != null }

        // confirm launch items retrieved matches total number
        assertTrue { results?.size == numTotal }
    }

}
















