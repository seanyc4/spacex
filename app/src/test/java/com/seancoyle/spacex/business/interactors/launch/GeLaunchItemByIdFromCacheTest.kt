package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.business.interactors.launch.GetLaunchItemByIdFromCache.Companion.GET_LAUNCH_ITEM_BY_ID_SUCCESS
import com.seancoyle.spacex.di.LaunchDependencies
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/*
Test cases:
1. getLaunchItemsByIdFromCache_success_confirmCorrect()
    a) get a launch item from the db with a specific id
    b) listen for GET_LAUNCH_ITEM_BY_ID_SUCCESS from flow emission
    c) compare the id to requested with the id of the launch item we retrieved
*/
@InternalCoroutinesApi
class GeLaunchItemByIdFromCacheTest {

    // system in test
    private val getLaunchItemById: GetLaunchItemByIdFromCache

    // dependencies
    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private val cacheDataSource: LaunchCacheDataSource
    private val factory: LaunchFactory

    init {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        factory = launchDependencies.launchFactory
        getLaunchItemById = GetLaunchItemByIdFromCache(
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun getLaunchItemsByIdFromCache_success_confirmCorrect() = runBlocking {

        val id = 1
        var retrievedLaunch: LaunchModel? = null

        getLaunchItemById.execute(
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
















