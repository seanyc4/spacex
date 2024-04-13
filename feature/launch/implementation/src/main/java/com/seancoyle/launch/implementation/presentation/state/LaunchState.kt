package com.seancoyle.launch.implementation.presentation.state

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import kotlinx.parcelize.Parcelize

internal sealed interface LaunchesUiState {
    @Stable
    data class Success(
        val launches: List<LaunchTypes>,
        val paginationState: PaginationState = PaginationState.None,
        val notificationState: NotificationState? = null
    ) : LaunchesUiState

    data object Loading : LaunchesUiState

    data class Error(
        val errorNotificationState: NotificationState? = null
    ) : LaunchesUiState
}

internal sealed interface PaginationState {
    data object None : PaginationState
    data object Loading : PaginationState
    data object Error : PaginationState
}

@Parcelize
@Stable
data class LaunchesFilterState(
    val order: Order = Order.DESC,
    val launchStatus: LaunchStatus = LaunchStatus.ALL,
    val launchYear: String = "",
    val isDialogFilterDisplayed: Boolean = false,
) : Parcelable

@Parcelize
@Stable
internal data class LaunchesScrollState(
    val page: Int = 1,
    val scrollPosition: Int = 0
) : Parcelable