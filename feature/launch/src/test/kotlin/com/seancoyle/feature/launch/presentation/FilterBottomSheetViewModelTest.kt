package com.seancoyle.feature.launch.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.domain.usecase.analytics.LaunchAnalyticsComponent
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.filter.FilterBottomSheetEvent
import com.seancoyle.feature.launch.presentation.launches.filter.FilterBottomSheetViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class FilterBottomSheetViewModelTest {

    @get:Rule
    val dispatcherRule = TestCoroutineRule()

    @MockK(relaxed = true)
    private lateinit var launchAnalyticsComponent: LaunchAnalyticsComponent

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var underTest: FilterBottomSheetViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        savedStateHandle = SavedStateHandle()
    }

    private fun createViewModel(
        initialQuery: String = "",
        initialStatus: LaunchStatus = LaunchStatus.ALL
    ): FilterBottomSheetViewModel {
        return FilterBottomSheetViewModel(
            initialQuery = initialQuery,
            initialStatus = initialStatus,
            launchAnalyticsComponent = launchAnalyticsComponent,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `GIVEN initial values WHEN ViewModel is created THEN state reflects those values`() {
        underTest = createViewModel(
            initialQuery = "Starlink",
            initialStatus = LaunchStatus.SUCCESS
        )

        assertEquals("Starlink", underTest.state.query)
        assertEquals(LaunchStatus.SUCCESS, underTest.state.selectedStatus)
    }

    @Test
    fun `GIVEN empty initial values WHEN ViewModel is created THEN state has defaults`() {
        underTest = createViewModel()

        assertEquals("", underTest.state.query)
        assertEquals(LaunchStatus.ALL, underTest.state.selectedStatus)
        assertFalse(underTest.state.hasActiveFilters)
        assertEquals(0, underTest.state.activeFilterCount)
    }

    @Test
    fun `GIVEN empty query WHEN QueryChanged event THEN state query is updated`() {
        underTest = createViewModel()

        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("Falcon"))

        assertEquals("Falcon", underTest.state.query)
    }

    @Test
    fun `GIVEN existing query WHEN QueryChanged with new value THEN state query is updated`() {
        underTest = createViewModel(initialQuery = "Dragon")

        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("Starship"))

        assertEquals("Starship", underTest.state.query)
    }

    @Test
    fun `GIVEN query WHEN hasActiveFilters checked THEN returns true`() {
        underTest = createViewModel()

        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("Test"))

        assertTrue(underTest.state.hasActiveFilters)
        assertEquals(1, underTest.state.activeFilterCount)
    }

    @Test
    fun `GIVEN ALL status WHEN StatusSelected with SUCCESS THEN status is SUCCESS`() {
        underTest = createViewModel()

        underTest.onEvent(FilterBottomSheetEvent.StatusSelected(LaunchStatus.SUCCESS))

        assertEquals(LaunchStatus.SUCCESS, underTest.state.selectedStatus)
    }

    @Test
    fun `GIVEN SUCCESS status WHEN StatusSelected with same status THEN toggles to ALL`() {
        underTest = createViewModel(initialStatus = LaunchStatus.SUCCESS)

        underTest.onEvent(FilterBottomSheetEvent.StatusSelected(LaunchStatus.SUCCESS))

        assertEquals(LaunchStatus.ALL, underTest.state.selectedStatus)
    }

    @Test
    fun `GIVEN SUCCESS status WHEN StatusSelected with different status THEN status changes`() {
        underTest = createViewModel(initialStatus = LaunchStatus.SUCCESS)

        underTest.onEvent(FilterBottomSheetEvent.StatusSelected(LaunchStatus.FAILED))

        assertEquals(LaunchStatus.FAILED, underTest.state.selectedStatus)
    }

    @Test
    fun `GIVEN ALL status WHEN StatusSelected with ALL THEN remains ALL`() {
        underTest = createViewModel()

        underTest.onEvent(FilterBottomSheetEvent.StatusSelected(LaunchStatus.ALL))

        assertEquals(LaunchStatus.ALL, underTest.state.selectedStatus)
    }

    @Test
    fun `GIVEN non-ALL status WHEN hasActiveFilters checked THEN returns true`() {
        underTest = createViewModel()

        underTest.onEvent(FilterBottomSheetEvent.StatusSelected(LaunchStatus.GO))

        assertTrue(underTest.state.hasActiveFilters)
        assertEquals(1, underTest.state.activeFilterCount)
    }

    @Test
    fun `GIVEN filters set WHEN ApplyFilters event THEN filterResult is emitted`() = runTest {
        underTest = createViewModel()
        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("Starlink"))
        underTest.onEvent(FilterBottomSheetEvent.StatusSelected(LaunchStatus.SUCCESS))

        underTest.filterResult.test {
            underTest.onEvent(FilterBottomSheetEvent.ApplyFilters)
            testScheduler.advanceUntilIdle()

            val result = awaitItem()
            assertEquals("Starlink", result.query)
            assertEquals(LaunchStatus.SUCCESS, result.status)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN query with spaces WHEN ApplyFilters THEN query is trimmed`() = runTest {
        underTest = createViewModel()
        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("  Starlink  "))

        underTest.filterResult.test {
            underTest.onEvent(FilterBottomSheetEvent.ApplyFilters)
            testScheduler.advanceUntilIdle()

            val result = awaitItem()
            assertEquals("Starlink", result.query)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN query applied WHEN ApplyFilters THEN query added to recent searches`() = runTest {
        underTest = createViewModel()
        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("Dragon"))

        underTest.filterResult.test {
            underTest.onEvent(FilterBottomSheetEvent.ApplyFilters)
            testScheduler.advanceUntilIdle()

            awaitItem()
            assertTrue(underTest.state.recentSearches.contains("Dragon"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN active filters WHEN ClearAllFilters event THEN state is reset`() {
        underTest = createViewModel(
            initialQuery = "Starship",
            initialStatus = LaunchStatus.FAILED
        )
        assertTrue(underTest.state.hasActiveFilters)

        underTest.onEvent(FilterBottomSheetEvent.ClearAllFilters)

        assertEquals("", underTest.state.query)
        assertEquals(LaunchStatus.ALL, underTest.state.selectedStatus)
        assertFalse(underTest.state.hasActiveFilters)
    }

    @Test
    fun `GIVEN collapsed state WHEN ToggleExpanded event THEN state is expanded`() {
        underTest = createViewModel()
        assertFalse(underTest.state.isExpanded)

        underTest.onEvent(FilterBottomSheetEvent.ToggleExpanded)

        assertTrue(underTest.state.isExpanded)
    }

    @Test
    fun `GIVEN expanded state WHEN ToggleExpanded event THEN state is collapsed`() {
        underTest = createViewModel()
        underTest.onEvent(FilterBottomSheetEvent.ToggleExpanded)
        assertTrue(underTest.state.isExpanded)

        underTest.onEvent(FilterBottomSheetEvent.ToggleExpanded)

        assertFalse(underTest.state.isExpanded)
    }

    @Test
    fun `GIVEN recent search WHEN RecentSearchSelected event THEN query is updated`() {
        underTest = createViewModel()

        underTest.onEvent(FilterBottomSheetEvent.RecentSearchSelected("Dragon"))

        assertEquals("Dragon", underTest.state.query)
    }

    @Test
    fun `WHEN Dismiss event THEN dismissEvent is emitted`() = runTest {
        underTest = createViewModel()

        underTest.dismissEvent.test {
            underTest.onEvent(FilterBottomSheetEvent.Dismiss)
            testScheduler.advanceUntilIdle()

            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN query and status WHEN activeFilterCount checked THEN returns 2`() {
        underTest = createViewModel()

        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("Test"))
        underTest.onEvent(FilterBottomSheetEvent.StatusSelected(LaunchStatus.SUCCESS))

        assertEquals(2, underTest.state.activeFilterCount)
    }

    @Test
    fun `GIVEN only query WHEN activeFilterCount checked THEN returns 1`() {
        underTest = createViewModel()

        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("Test"))

        assertEquals(1, underTest.state.activeFilterCount)
    }

    @Test
    fun `GIVEN no filters WHEN activeFilterCount checked THEN returns 0`() {
        underTest = createViewModel()

        assertEquals(0, underTest.state.activeFilterCount)
    }

    @Test
    fun `GIVEN filters applied WHEN ApplyFilters THEN logs filter_apply event`() = runTest {
        underTest = createViewModel()
        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("SpaceX"))
        underTest.onEvent(FilterBottomSheetEvent.StatusSelected(LaunchStatus.SUCCESS))

        underTest.filterResult.test {
            underTest.onEvent(FilterBottomSheetEvent.ApplyFilters)
            testScheduler.advanceUntilIdle()

            awaitItem()
            verify {
                launchAnalyticsComponent.trackFilterApply(
                    status = LaunchStatus.SUCCESS.name,
                    hasQuery = true,
                    queryLength = 6,
                    filterCount = 2
                )
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN active filters WHEN ClearAllFilters THEN logs filter_clear event`() {
        underTest = createViewModel(
            initialQuery = "Test",
            initialStatus = LaunchStatus.SUCCESS
        )

        underTest.onEvent(FilterBottomSheetEvent.ClearAllFilters)

        verify {
            launchAnalyticsComponent.trackFilterClear(filterCount = 2)
        }
    }

    @Test
    fun `GIVEN recent search WHEN RecentSearchSelected THEN logs recent_search_tap event`() {
        underTest = createViewModel()

        underTest.onEvent(FilterBottomSheetEvent.RecentSearchSelected("Dragon"))

        verify {
            launchAnalyticsComponent.trackRecentSearchTap()
        }
    }

    @Test
    fun `GIVEN no filters WHEN ApplyFilters THEN logs filter_apply with zero count`() = runTest {
        underTest = createViewModel()

        underTest.filterResult.test {
            underTest.onEvent(FilterBottomSheetEvent.ApplyFilters)
            testScheduler.advanceUntilIdle()

            awaitItem()
            verify {
                launchAnalyticsComponent.trackFilterApply(
                    status = LaunchStatus.ALL.name,
                    hasQuery = false,
                    queryLength = 0,
                    filterCount = 0
                )
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}
