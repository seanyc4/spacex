package com.seancoyle.launch.implementation.presentation

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.seancoyle.core.presentation.NotificationState
import com.seancoyle.launch.api.LaunchConstants.ORDER_ASC
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import kotlinx.parcelize.Parcelize

internal sealed interface LaunchUiState {
    @Stable
    data class Success(
        val launches: List<LaunchTypes>,
        val paginationState: PaginationState = PaginationState.None,
        val notificationState: NotificationState? = null
    ) : LaunchUiState

    data object Loading : LaunchUiState

    data class Error(
        val errorNotificationState: NotificationState? = null
    ) : LaunchUiState
}

internal sealed interface PaginationState {
    data object None : PaginationState
    data object Loading : PaginationState
    data object Error : PaginationState
}

@Parcelize
@Stable
data class LaunchFilterState(
    val isDialogFilterDisplayed: Boolean = false,
    val launchFilter: LaunchStatus = LaunchStatus.ALL,
    val order: String = ORDER_ASC,
    val year: String = "",
) : Parcelable

@Parcelize
@Stable
internal data class LaunchesListState(
    val page: Int = 1,
    val scrollPosition: Int = 0
) : Parcelable