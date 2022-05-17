package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.business.interactors.launch.GetNumLaunchItemsFromCache.Companion.GET_NUM_LAUNCH_ITEMS_SUCCESS
import com.seancoyle.spacex.di.LaunchDependencies
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/*
Test cases:
1. getNumLaunchItems_success_confirmCorrect()
    a) get the number of launch in cache
    b) listen for GET_NUM_LAUNCH_ITEMS_SUCCESS from flow emission
    c) compare with the number of launch in the fake data set
*/
@InternalCoroutinesApi
class GetNumLaunchItemsFromCacheTest {

    // system in test
    private val getNumLaunchItemsFromCache: GetNumLaunchItemsFromCache

    // dependencies
    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private val cacheDataSource: LaunchCacheDataSource
    private val factory: LaunchFactory

    init {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        factory = launchDependencies.launchFactory
        getNumLaunchItemsFromCache = GetNumLaunchItemsFromCache(
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun getNumLaunchItems_success_confirmCorrect() = runBlocking {

        var numItems = 0
        getNumLaunchItemsFromCache.execute(
            stateEvent = GetNumLaunchItemsInCacheEvent
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
















