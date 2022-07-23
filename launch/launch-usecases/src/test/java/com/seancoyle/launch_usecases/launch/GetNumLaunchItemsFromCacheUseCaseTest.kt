package com.seancoyle.launch_usecases.launch

import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_models.model.launch.LaunchFactory
import com.seancoyle.launch_usecases.di.LaunchDependencies
import com.seancoyle.launch_usecases.launch.GetNumLaunchItemsFromCacheUseCase.Companion.GET_NUM_LAUNCH_ITEMS_SUCCESS
import com.seancoyle.launch_viewstate.LaunchStateEvent
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
class GetNumLaunchItemsFromCacheUseCaseTest {

    // system in test
    private val getNumLaunchItemsFromCacheUseCase: GetNumLaunchItemsFromCacheUseCase

    // dependencies
    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private val cacheDataSource: LaunchCacheDataSource
    private val factory: LaunchFactory

    init {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        factory = launchDependencies.launchFactory
        getNumLaunchItemsFromCacheUseCase = GetNumLaunchItemsFromCacheUseCase(
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
















