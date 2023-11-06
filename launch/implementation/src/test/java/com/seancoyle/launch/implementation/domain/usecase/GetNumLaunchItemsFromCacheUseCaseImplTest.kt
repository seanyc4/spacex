package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_SUCCESS
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.implementation.data.cache.LaunchCacheDataSource
import com.seancoyle.launch.implementation.presentation.LaunchEvents
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetNumLaunchItemsFromCacheUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var cacheDataSource: LaunchCacheDataSource

    private lateinit var underTest: GetNumLaunchItemsFromCacheUseCase

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetNumLaunchItemsFromCacheUseCaseImpl(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            cacheDataSource = cacheDataSource
        )
    }

    @Test
    fun getNumLaunchItems_success_confirmCorrect() = runBlocking {

        var numItems = 0
        var stateMessage: String? = null
        coEvery { cacheDataSource.getTotalEntries() } returns TOTAL_ENTRIES

        underTest(
            event = LaunchEvents.GetNumLaunchItemsInCacheEvent
        ).collect { value ->
            numItems = value?.data?.numLaunchesInCache ?: 0
            stateMessage = value?.stateMessage?.response?.message
        }

        val expectedMessage = LaunchEvents.GetNumLaunchItemsInCacheEvent.eventName() + EVENT_CACHE_SUCCESS
        assertEquals(expectedMessage, stateMessage)
        assertEquals(TOTAL_ENTRIES, numItems)
    }

    companion object {
        private const val TOTAL_ENTRIES = 10
    }
}