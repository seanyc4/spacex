package com.seancoyle.feature.launch.implementation.presentation.state

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.presentation.model.BottomSheetLinksUi
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchTypesUiModel
import kotlinx.parcelize.Parcelize

internal sealed interface LaunchesUiState {
    @Stable
    data class Success(
        val launches: List<LaunchTypesUiModel>
    ) : LaunchesUiState

    data object Loading : LaunchesUiState

    data class Error(
        val errorNotificationState: NotificationState? = null
    ) : LaunchesUiState
}

internal sealed interface PaginationState {
    data object Idle : PaginationState
    data object Loading : PaginationState
    data object Error : PaginationState
    data object EndReached : PaginationState
}

@Parcelize
@Stable
internal data class LaunchesFilterState(
    val order: Order = Order.DESC,
    val launchStatus: LaunchStatus = LaunchStatus.ALL,
    val launchYear: String = "",
    val isVisible: Boolean = false,
) : Parcelable

@Parcelize
@Stable
internal data class LaunchesScrollState(
    val scrollPosition: Int = 0
) : Parcelable

@Parcelize
@Stable
internal data class BottomSheetUiState(
    val isVisible: Boolean = false,
    val bottomSheetLinks: List<BottomSheetLinksUi> = emptyList()
) : Parcelable
