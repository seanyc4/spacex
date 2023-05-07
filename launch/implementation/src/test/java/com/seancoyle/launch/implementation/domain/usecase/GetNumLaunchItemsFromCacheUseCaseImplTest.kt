package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_SUCCESS
import com.seancoyle.launch.api.LaunchCacheDataSource
import com.seancoyle.launch.api.model.LaunchFactory
import com.seancoyle.launch.api.usecase.GetNumLaunchItemsFromCacheUseCase
import com.seancoyle.launch.implementation.domain.GetNumLaunchItemsFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.LaunchDependencies
import com.seancoyle.launch.implementation.presentation.LaunchEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetNumLaunchItemsFromCacheUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val launchDependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    private lateinit var factory: LaunchFactory
    private lateinit var underTest: GetNumLaunchItemsFromCacheUseCase

    @BeforeEach
    fun setup() {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
        factory = launchDependencies.launchFactory
        underTest =
            GetNumLaunchItemsFromCacheUseCaseImpl(
                ioDispatcher = mainCoroutineRule.testDispatcher,
                cacheDataSource = cacheDataSource
            )
    }

    @Test
    fun getNumLaunchItems_success_confirmCorrect() = runBlocking {

        var numItems = 0
        underTest(
            event = LaunchEvent.GetNumLaunchItemsInCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvent.GetNumLaunchItemsInCacheEvent.eventName() + EVENT_CACHE_SUCCESS
            )
            numItems = value?.data?.numLaunchItemsInCache ?: 0
        }

        val actualNumItemsInCache = cacheDataSource.getTotalEntries()
        assertTrue { actualNumItemsInCache == numItems }
    }
}
















