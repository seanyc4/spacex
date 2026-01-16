package com.seancoyle.feature.launch.presentation.launches.filter

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class FilterBottomSheetState(
    val query: String = "",
    val selectedStatus: LaunchStatus = LaunchStatus.ALL,
    val isExpanded: Boolean = false,
    val recentSearches: List<String> = emptyList(),
) : Parcelable {

    val hasActiveFilters: Boolean
        get() = query.isNotBlank() || selectedStatus != LaunchStatus.ALL

    val activeFilterCount: Int
        get() = listOfNotNull(
            query.takeIf { it.isNotBlank() },
            selectedStatus.takeIf { it != LaunchStatus.ALL }
        ).size
}

sealed interface FilterBottomSheetEvent {
    data class QueryChanged(val query: String) : FilterBottomSheetEvent
    data class StatusSelected(val status: LaunchStatus) : FilterBottomSheetEvent
    data object ApplyFilters : FilterBottomSheetEvent
    data object ClearAllFilters : FilterBottomSheetEvent
    data object Dismiss : FilterBottomSheetEvent
    data object ToggleExpanded : FilterBottomSheetEvent
    data class RecentSearchSelected(val query: String) : FilterBottomSheetEvent
}

data class FilterResult(
    val query: String,
    val status: LaunchStatus
)
