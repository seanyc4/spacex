package com.seancoyle.launch.implementation.presentation

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.seancoyle.core.domain.Response
import com.seancoyle.launch.api.LaunchConstants.ORDER_ASC
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.parcelize.Parcelize

internal sealed interface LaunchUiState {
    @Stable
    data class Success(
        val launches: List<ViewType>,
        val paginationState: PaginationState = PaginationState.None
    ) : LaunchUiState

    data object Loading : LaunchUiState

    data class Error(
        val errorResponse: Response? = null,
        val showError: Boolean = true
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