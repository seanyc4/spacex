package com.seancoyle.feature.launch.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.LaunchesViewModel
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.launches.state.PagingEvents
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Tests for LaunchesViewModel focusing on the new architecture where:
 * - Each tab has its own independent Pager flow
 * - Tab switching is a pure UI concern (no Pager invalidation)
 * - Scroll positions are maintained per tab
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LaunchesViewModelTest {

    @MockK
    private lateinit var launchesComponent: LaunchesComponent

    @MockK
    private lateinit var uiMapper: LaunchUiMapper

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var underTest: LaunchesViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        savedStateHandle = SavedStateHandle()

        // Mock the use case to return empty paging data for both tabs
        every {
            launchesComponent.observeUpcomingLaunches(any())
        } returns flowOf(PagingData.empty())

        every {
            launchesComponent.observePastLaunches(any())
        } returns flowOf(PagingData.empty())

        underTest = LaunchesViewModel(
            launchesComponent = launchesComponent,
            uiMapper = uiMapper,
            savedStateHandle = savedStateHandle
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has UPCOMING as default launch type`() {
        assertEquals(LaunchesType.UPCOMING, underTest.screenState.launchesType)
    }

    @Test
    fun `initial state has zero scroll positions for both tabs`() {
        assertEquals(0, underTest.screenState.upcomingScrollPosition)
        assertEquals(0, underTest.screenState.pastScrollPosition)
    }

    @Test
    fun `upcomingLaunchesFlow is not null`() {
        assertNotNull(underTest.upcomingLaunchesFlow)
    }

    @Test
    fun `pastLaunchesFlow is not null`() {
        assertNotNull(underTest.pastLaunchesFlow)
    }

    @Test
    fun `onTabSelected updates launchesType in state`() = runTest {
        // Start with UPCOMING (default)
        assertEquals(LaunchesType.UPCOMING, underTest.screenState.launchesType)

        // Select PAST tab
        underTest.onEvent(LaunchesEvents.TabSelectedEvent(LaunchesType.PAST))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(LaunchesType.PAST, underTest.screenState.launchesType)
    }

    @Test
    fun `onTabSelected does not affect scroll positions`() = runTest {
        // Set scroll positions for both tabs
        underTest.updateScrollPosition(LaunchesType.UPCOMING, 50)
        underTest.updateScrollPosition(LaunchesType.PAST, 100)

        // Switch tabs
        underTest.onEvent(LaunchesEvents.TabSelectedEvent(LaunchesType.PAST))
        testDispatcher.scheduler.advanceUntilIdle()

        // Scroll positions should be preserved
        assertEquals(50, underTest.screenState.upcomingScrollPosition)
        assertEquals(100, underTest.screenState.pastScrollPosition)
    }

    @Test
    fun `updateScrollPosition updates only the specified tab's scroll position`() {
        underTest.updateScrollPosition(LaunchesType.UPCOMING, 42)

        assertEquals(42, underTest.screenState.upcomingScrollPosition)
        assertEquals(0, underTest.screenState.pastScrollPosition) // Unchanged
    }

    @Test
    fun `updateScrollPosition for PAST tab updates only past scroll position`() {
        underTest.updateScrollPosition(LaunchesType.PAST, 99)

        assertEquals(0, underTest.screenState.upcomingScrollPosition) // Unchanged
        assertEquals(99, underTest.screenState.pastScrollPosition)
    }

    @Test
    fun `onPullToRefresh sends Refresh event to the correct channel for UPCOMING tab`() = runTest {
        val events = mutableListOf<PagingEvents>()
        val job = launch {
            underTest.upcomingPagingEvents.toList(events)
        }

        underTest.onEvent(LaunchesEvents.PullToRefreshEvent)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, events.size)
        assertEquals(PagingEvents.Refresh, events[0])

        job.cancel()
    }

    @Test
    fun `onPullToRefresh sends Refresh event to PAST channel when on PAST tab`() = runTest {
        // Switch to PAST tab first
        underTest.onEvent(LaunchesEvents.TabSelectedEvent(LaunchesType.PAST))
        testDispatcher.scheduler.advanceUntilIdle()

        val events = mutableListOf<PagingEvents>()
        val job = launch {
            underTest.pastPagingEvents.toList(events)
        }

        underTest.onEvent(LaunchesEvents.PullToRefreshEvent)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, events.size)
        assertEquals(PagingEvents.Refresh, events[0])

        job.cancel()
    }

    @Test
    fun `onRetryFetch sends Retry event to correct channel for UPCOMING tab`() = runTest {
        val events = mutableListOf<PagingEvents>()
        val job = launch {
            underTest.upcomingPagingEvents.toList(events)
        }

        underTest.onEvent(LaunchesEvents.RetryFetchEvent)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, events.size)
        assertEquals(PagingEvents.Retry, events[0])

        job.cancel()
    }

    @Test
    fun `setRefreshing updates isRefreshing state`() {
        underTest.setRefreshing(true)
        assertEquals(true, underTest.screenState.isRefreshing)

        underTest.setRefreshing(false)
        assertEquals(false, underTest.screenState.isRefreshing)
    }

    @Test
    fun `DisplayFilterDialogEvent shows filter dialog`() = runTest {
        underTest.onEvent(LaunchesEvents.DisplayFilterDialogEvent)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(true, underTest.screenState.isFilterDialogVisible)
    }

    @Test
    fun `DismissFilterDialogEvent hides filter dialog`() = runTest {
        underTest.onEvent(LaunchesEvents.DisplayFilterDialogEvent)
        testDispatcher.scheduler.advanceUntilIdle()

        underTest.onEvent(LaunchesEvents.DismissFilterDialogEvent)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, underTest.screenState.isFilterDialogVisible)
    }

    @Test
    fun `UpdateFilterStateEvent updates query and launch status`() = runTest {
        underTest.onEvent(LaunchesEvents.UpdateFilterStateEvent(
            launchStatus = LaunchStatus.SUCCESS,
            query = "SpaceX"
        ))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("SpaceX", underTest.screenState.query)
        assertEquals(LaunchStatus.SUCCESS, underTest.screenState.launchStatus)
    }
}

