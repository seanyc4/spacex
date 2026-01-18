package com.seancoyle.feature.launch.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import app.cash.turbine.test
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.LaunchesViewModel
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.launches.state.PagingEvents
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchesViewModelTest {

    @get:Rule
    val dispatcherRule = TestCoroutineRule()

    @MockK
    private lateinit var launchesComponent: LaunchesComponent

    @MockK
    private lateinit var uiMapper: LaunchUiMapper

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var underTest: LaunchesViewModel


    @Before
    fun setup() {
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

    @Test
    fun `GIVEN ViewModel created WHEN initial state observed THEN has UPCOMING as default launch type`() {
        assertEquals(LaunchesType.UPCOMING, underTest.screenState.launchesType)
    }

    @Test
    fun `GIVEN ViewModel created WHEN initial state observed THEN has zero scroll positions for both tabs`() {
        assertEquals(0, underTest.screenState.upcomingScrollPosition)
        assertEquals(0, underTest.screenState.pastScrollPosition)
    }

    @Test
    fun `GIVEN ViewModel created WHEN upcomingLaunchesFlow accessed THEN is not null`() {
        assertNotNull(underTest.upcomingLaunchesFlow)
    }

    @Test
    fun `GIVEN ViewModel created WHEN pastLaunchesFlow accessed THEN is not null`() {
        assertNotNull(underTest.pastLaunchesFlow)
    }

    @Test
    fun `GIVEN UPCOMING tab selected WHEN onTabSelected with PAST THEN updates launchesType in state`() = runTest {
        // Start with UPCOMING (default)
        assertEquals(LaunchesType.UPCOMING, underTest.screenState.launchesType)

        // Select PAST tab
        underTest.onEvent(LaunchesEvents.TabSelectedEvent(LaunchesType.PAST))
        testScheduler.advanceUntilIdle()

        assertEquals(LaunchesType.PAST, underTest.screenState.launchesType)
    }

    @Test
    fun `GIVEN scroll positions set WHEN onTabSelected THEN does not affect scroll positions`() = runTest {
        // Set scroll positions for both tabs
        underTest.updateScrollPosition(LaunchesType.UPCOMING, 50)
        underTest.updateScrollPosition(LaunchesType.PAST, 100)

        // Switch tabs
        underTest.onEvent(LaunchesEvents.TabSelectedEvent(LaunchesType.PAST))
        testScheduler.advanceUntilIdle()

        // Scroll positions should be preserved
        assertEquals(50, underTest.screenState.upcomingScrollPosition)
        assertEquals(100, underTest.screenState.pastScrollPosition)
    }

    @Test
    fun `GIVEN UPCOMING tab WHEN updateScrollPosition THEN updates only upcoming scroll position`() {
        underTest.updateScrollPosition(LaunchesType.UPCOMING, 42)

        assertEquals(42, underTest.screenState.upcomingScrollPosition)
        assertEquals(0, underTest.screenState.pastScrollPosition) // Unchanged
    }

    @Test
    fun `GIVEN PAST tab WHEN updateScrollPosition THEN updates only past scroll position`() {
        underTest.updateScrollPosition(LaunchesType.PAST, 99)

        assertEquals(0, underTest.screenState.upcomingScrollPosition) // Unchanged
        assertEquals(99, underTest.screenState.pastScrollPosition)
    }

    @Test
    fun `GIVEN UPCOMING tab selected WHEN onPullToRefresh THEN sends Refresh event to upcoming channel`() = runTest {
        underTest.upcomingPagingEvents.test {
            underTest.onEvent(LaunchesEvents.PullToRefreshEvent)
            testScheduler.advanceUntilIdle()

            assertEquals(PagingEvents.Refresh, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN PAST tab selected WHEN onPullToRefresh THEN sends Refresh event to past channel`() = runTest {
        // Switch to PAST tab first
        underTest.onEvent(LaunchesEvents.TabSelectedEvent(LaunchesType.PAST))
        testScheduler.advanceUntilIdle()

        underTest.pastPagingEvents.test {
            underTest.onEvent(LaunchesEvents.PullToRefreshEvent)
            testScheduler.advanceUntilIdle()

            assertEquals(PagingEvents.Refresh, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN UPCOMING tab selected WHEN onRetryFetch THEN sends Retry event to upcoming channel`() = runTest {
        underTest.upcomingPagingEvents.test {
            underTest.onEvent(LaunchesEvents.RetryFetchEvent)
            testScheduler.advanceUntilIdle()

            assertEquals(PagingEvents.Retry, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN isRefreshing false WHEN setRefreshing true THEN updates isRefreshing state`() {
        underTest.setRefreshing(true)
        assertEquals(true, underTest.screenState.isRefreshing)

        underTest.setRefreshing(false)
        assertEquals(false, underTest.screenState.isRefreshing)
    }

    @Test
    fun `GIVEN filter bottom sheet hidden WHEN DisplayFilterBottomSheetEvent THEN shows filter bottom sheet`() = runTest {
        underTest.onEvent(LaunchesEvents.DisplayFilterBottomSheetEvent)
        testScheduler.advanceUntilIdle()

        assertEquals(true, underTest.screenState.isFilterBottomSheetVisible)
    }

    @Test
    fun `GIVEN filter bottom sheet visible WHEN DismissFilterBottomSheetEvent THEN hides filter bottom sheet`() = runTest {
        underTest.onEvent(LaunchesEvents.DisplayFilterBottomSheetEvent)
        testScheduler.advanceUntilIdle()

        underTest.onEvent(LaunchesEvents.DismissFilterBottomSheetEvent)
        testScheduler.advanceUntilIdle()

        assertEquals(false, underTest.screenState.isFilterBottomSheetVisible)
    }

    @Test
    fun `GIVEN filter values WHEN UpdateFilterStateEvent THEN updates query and launch status`() = runTest {
        underTest.onEvent(LaunchesEvents.UpdateFilterStateEvent(
            launchStatus = LaunchStatus.SUCCESS,
            query = "SpaceX"
        ))
        testScheduler.advanceUntilIdle()

        assertEquals("SpaceX", underTest.screenState.query)
        assertEquals(LaunchStatus.SUCCESS, underTest.screenState.launchStatus)
    }

    @Test
    fun `GIVEN pull to refresh WHEN event fired THEN clears query and status`() = runTest {
        underTest.onEvent(LaunchesEvents.UpdateFilterStateEvent(
            launchStatus = LaunchStatus.SUCCESS,
            query = "SpaceX"
        ))
        testScheduler.advanceUntilIdle()

        underTest.onEvent(LaunchesEvents.PullToRefreshEvent)
        testScheduler.advanceUntilIdle()

        assertEquals("", underTest.screenState.query)
        assertEquals(LaunchStatus.ALL, underTest.screenState.launchStatus)
    }

    @Test
    fun `GIVEN pull to refresh WHEN event fired THEN sets isRefreshing true`() = runTest {
        underTest.onEvent(LaunchesEvents.PullToRefreshEvent)
        testScheduler.advanceUntilIdle()

        assertEquals(true, underTest.screenState.isRefreshing)
    }

    @Test
    fun `GIVEN PAST tab selected WHEN onRetryFetch THEN sends Retry event to past channel`() = runTest {
        underTest.onEvent(LaunchesEvents.TabSelectedEvent(LaunchesType.PAST))
        testScheduler.advanceUntilIdle()

        underTest.pastPagingEvents.test {
            underTest.onEvent(LaunchesEvents.RetryFetchEvent)
            testScheduler.advanceUntilIdle()

            assertEquals(PagingEvents.Retry, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN multiple tab switches WHEN performed THEN preserves all scroll positions`() = runTest {
        underTest.updateScrollPosition(LaunchesType.UPCOMING, 100)
        underTest.onEvent(LaunchesEvents.TabSelectedEvent(LaunchesType.PAST))
        testScheduler.advanceUntilIdle()

        underTest.updateScrollPosition(LaunchesType.PAST, 200)
        underTest.onEvent(LaunchesEvents.TabSelectedEvent(LaunchesType.UPCOMING))
        testScheduler.advanceUntilIdle()

        assertEquals(100, underTest.screenState.upcomingScrollPosition)
        assertEquals(200, underTest.screenState.pastScrollPosition)
    }

    @Test
    fun `GIVEN launchesQueryState WHEN status is ALL THEN passes null to query`() = runTest {
        underTest.onEvent(LaunchesEvents.UpdateFilterStateEvent(
            launchStatus = LaunchStatus.ALL,
            query = ""
        ))
        testScheduler.advanceUntilIdle()

        val queryState = underTest.launchesQueryState.value
        assertEquals("", queryState.query)
        assertEquals(null, queryState.status)
    }

    @Test
    fun `GIVEN launchesQueryState WHEN status is not ALL THEN passes status to query`() = runTest {
        underTest.onEvent(LaunchesEvents.UpdateFilterStateEvent(
            launchStatus = LaunchStatus.SUCCESS,
            query = "Test"
        ))
        testScheduler.advanceUntilIdle()

        val queryState = underTest.launchesQueryState.value
        assertEquals("Test", queryState.query)
        assertEquals(LaunchStatus.SUCCESS, queryState.status)
    }
}
