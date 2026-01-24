package com.seancoyle.feature.launch.presentation

import androidx.lifecycle.SavedStateHandle
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.filter.FilterBottomSheetEvent
import com.seancoyle.feature.launch.presentation.launches.filter.FilterBottomSheetViewModel
import com.seancoyle.feature.launch.presentation.launches.filter.FilterResult
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class FilterBottomSheetViewModelTest {

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var underTest: FilterBottomSheetViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        savedStateHandle = SavedStateHandle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(
        initialQuery: String = "",
        initialStatus: LaunchStatus = LaunchStatus.ALL
    ): FilterBottomSheetViewModel {
        return FilterBottomSheetViewModel(
            initialQuery = initialQuery,
            initialStatus = initialStatus,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `GIVEN initial values WHEN ViewModel is created THEN state reflects those values`() {
        val initialQuery = "Starlink"
        val initialStatus = LaunchStatus.SUCCESS

        underTest = createViewModel(
            initialQuery = initialQuery,
            initialStatus = initialStatus
        )

        assertEquals(initialQuery, underTest.state.query)
        assertEquals(initialStatus, underTest.state.selectedStatus)
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
    fun `GIVEN filters set WHEN ApplyFilters event THEN filterResult is emitted`() = testScope.runTest {
        underTest = createViewModel()
        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("Starlink"))
        underTest.onEvent(FilterBottomSheetEvent.StatusSelected(LaunchStatus.SUCCESS))

        val results = mutableListOf<FilterResult>()
        val job = launch {
            underTest.filterResult.toList(results)
        }

        underTest.onEvent(FilterBottomSheetEvent.ApplyFilters)
        advanceUntilIdle()

        assertEquals(1, results.size)
        assertEquals("Starlink", results[0].query)
        assertEquals(LaunchStatus.SUCCESS, results[0].status)

        job.cancel()
    }

    @Test
    fun `GIVEN query with spaces WHEN ApplyFilters THEN query is trimmed`() = testScope.runTest {
        underTest = createViewModel()
        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("  Starlink  "))

        val results = mutableListOf<FilterResult>()
        val job = launch {
            underTest.filterResult.toList(results)
        }

        underTest.onEvent(FilterBottomSheetEvent.ApplyFilters)
        advanceUntilIdle()

        assertEquals("Starlink", results[0].query)

        job.cancel()
    }

    @Test
    fun `GIVEN query applied WHEN ApplyFilters THEN query added to recent searches`() = testScope.runTest {
        underTest = createViewModel()
        underTest.onEvent(FilterBottomSheetEvent.QueryChanged("Dragon"))

        val results = mutableListOf<FilterResult>()
        val job = launch {
            underTest.filterResult.toList(results)
        }

        underTest.onEvent(FilterBottomSheetEvent.ApplyFilters)
        advanceUntilIdle()

        assertTrue(underTest.state.recentSearches.contains("Dragon"))

        job.cancel()
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
        val recentSearch = "Dragon"

        underTest.onEvent(FilterBottomSheetEvent.RecentSearchSelected(recentSearch))

        assertEquals(recentSearch, underTest.state.query)
    }

    @Test
    fun `WHEN Dismiss event THEN dismissEvent is emitted`() = testScope.runTest {
        underTest = createViewModel()

        val dismissEvents = mutableListOf<Unit>()
        val job = launch {
            underTest.dismissEvent.toList(dismissEvents)
        }

        underTest.onEvent(FilterBottomSheetEvent.Dismiss)
        advanceUntilIdle()

        assertEquals(1, dismissEvents.size)

        job.cancel()
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
}
