package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core_testing.MainCoroutineRule
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
import com.seancoyle.launch.implementation.presentation.state.LaunchEvents
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

    private lateinit var underTest: GetNumLaunchItemsCacheUseCase

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetNumLaunchItemsCacheUseCaseImpl(
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
            event = LaunchEvents.GetNumLaunchesInCacheEvent
        ).collect { value ->
            numItems = value?.data?.numLaunchesInCache ?: 0
            stateMessage = value?.stateMessage?.response?.message
        }

        val expectedMessage = LaunchEvents.GetNumLaunchesInCacheEvent.eventName() + EVENT_CACHE_SUCCESS
        assertEquals(expectedMessage, stateMessage)
        assertEquals(TOTAL_ENTRIES, numItems)
    }

    companion object {
        private const val TOTAL_ENTRIES = 10
    }
}