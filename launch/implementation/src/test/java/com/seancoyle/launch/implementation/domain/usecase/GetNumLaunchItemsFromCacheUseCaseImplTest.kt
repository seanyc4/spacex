package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_SUCCESS
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.contract.data.LaunchCacheDataSource
import com.seancoyle.launch.contract.domain.usecase.GetNumLaunchItemsFromCacheUseCase
import com.seancoyle.launch.implementation.domain.GetNumLaunchItemsFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.LaunchDependencies
import com.seancoyle.launch.implementation.presentation.LaunchEvents
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
    private lateinit var underTest: GetNumLaunchItemsFromCacheUseCase

    @BeforeEach
    fun setup() {
        launchDependencies.build()
        cacheDataSource = launchDependencies.launchCacheDataSource
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
            event = LaunchEvents.GetNumLaunchItemsInCacheEvents
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.GetNumLaunchItemsInCacheEvents.eventName() + EVENT_CACHE_SUCCESS
            )
            numItems = value?.data?.numLaunchesInCache ?: 0
        }

        val actualNumItemsInCache = cacheDataSource.getTotalEntries()
        assertTrue { actualNumItemsInCache == numItems }
    }
}
















