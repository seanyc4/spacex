package com.seancoyle.feature.launch.presentation.launches.filter

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.seancoyle.feature.launch.domain.usecase.analytics.LaunchAnalyticsComponent
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

private const val TAG = "FilterBottomSheetVM"
private const val MAX_RECENT_SEARCHES = 5

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel(assistedFactory = FilterBottomSheetViewModel.Factory::class)
class FilterBottomSheetViewModel @AssistedInject constructor(
    @Assisted private val initialQuery: String,
    @Assisted private val initialStatus: LaunchStatus,
    private val launchAnalyticsComponent: LaunchAnalyticsComponent,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            initialQuery: String,
            initialStatus: LaunchStatus
        ): FilterBottomSheetViewModel
    }

    var state by savedStateHandle.saveable {
        mutableStateOf(
            FilterBottomSheetState(
                query = initialQuery,
                selectedStatus = initialStatus
            )
        )
    }
        private set

    private val _filterResult = MutableSharedFlow<FilterResult>(replay = 0)
    val filterResult: SharedFlow<FilterResult> = _filterResult.asSharedFlow()

    private val _dismissEvent = MutableSharedFlow<Unit>(replay = 0)
    val dismissEvent: SharedFlow<Unit> = _dismissEvent.asSharedFlow()

    fun onEvent(event: FilterBottomSheetEvent) {
        Timber.tag(TAG).d("Event received: $event")
        when (event) {
            is FilterBottomSheetEvent.QueryChanged -> onQueryChanged(event.query)
            is FilterBottomSheetEvent.StatusSelected -> onStatusSelected(event.status)
            is FilterBottomSheetEvent.ApplyFilters -> onApplyFilters()
            is FilterBottomSheetEvent.ClearAllFilters -> {
                launchAnalyticsComponent.trackFilterClear(state.activeFilterCount)
                onClearAllFilters()
            }
            is FilterBottomSheetEvent.Dismiss -> onDismiss()
            is FilterBottomSheetEvent.ToggleExpanded -> onToggleExpanded()
            is FilterBottomSheetEvent.RecentSearchSelected -> {
                launchAnalyticsComponent.trackRecentSearchTap()
                onRecentSearchSelected(event.query)
            }
        }
    }

    private fun onQueryChanged(query: String) {
        state = state.copy(query = query)
    }

    private fun onStatusSelected(status: LaunchStatus) {
        val newStatus = if (state.selectedStatus == status && status != LaunchStatus.ALL) {
            LaunchStatus.ALL
        } else {
            status
        }
        state = state.copy(selectedStatus = newStatus)
        Timber.tag(TAG).d("Status changed: $newStatus")
    }

    private fun onApplyFilters() {
        val result = FilterResult(
            query = state.query.trim(),
            status = state.selectedStatus
        )

        if (state.query.isNotBlank()) {
            val updatedRecent = (listOf(state.query.trim()) + state.recentSearches)
                .distinct()
                .take(MAX_RECENT_SEARCHES)
            state = state.copy(recentSearches = updatedRecent)
        }

        launchAnalyticsComponent.trackFilterApply(
            status = state.selectedStatus.name,
            hasQuery = state.query.isNotBlank(),
            queryLength = state.query.length,
            filterCount = state.activeFilterCount
        )

        Timber.tag(TAG).d("Applying filters: $result")
        viewModelScope.launch {
            _filterResult.emit(result)
            _dismissEvent.emit(Unit)
        }
    }

    private fun onClearAllFilters() {
        state = state.copy(
            query = "",
            selectedStatus = LaunchStatus.ALL
        )
        Timber.tag(TAG).d("Filters cleared")
    }

    private fun onDismiss() {
        viewModelScope.launch {
            _dismissEvent.emit(Unit)
        }
    }

    private fun onToggleExpanded() {
        state = state.copy(isExpanded = !state.isExpanded)
    }

    private fun onRecentSearchSelected(query: String) {
        state = state.copy(query = query)
    }
}
