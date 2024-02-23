package com.seancoyle.launch.implementation.presentation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.seancoyle.core.domain.Response
import com.seancoyle.launch.api.LaunchNetworkConstants.ORDER_ASC
import com.seancoyle.launch.implementation.domain.model.ViewType
import kotlinx.parcelize.Parcelize

internal sealed interface LaunchUiState {
    @Stable
    data class Success(
        val launches: List<ViewType>,
       // val filterState: FilterState,
        val paginationState: PaginationState = PaginationState.None
    ) : LaunchUiState

    data object Loading : LaunchUiState
    data class Error(
        val errorResponse: Response,
        val displayError: Boolean
    ) : LaunchUiState
}

internal sealed interface PaginationState {
    data object None : PaginationState
    data object Loading : PaginationState
    data object Error : PaginationState
}

/*sealed interface FilterState {
    data class Filter(
        val isDialogFilterDisplayed: Boolean = false,
        val launchFilter: Int? = null,
        val order: String = ORDER_ASC,
        val year: String = "",
    ) : FilterState
}*/

@Parcelize
@Immutable
internal data class FilterState(
    val isDialogFilterDisplayed: Boolean = false,
    val launchFilter: Int? = null,
    val order: String = ORDER_ASC,
    val year: String = "",
) : Parcelable

/*@Immutable
data class LaunchState(
    val mergedLaunches: List<ViewType> = emptyList(),
    val isLoading: Boolean = false,
    val errorResponse: Response? = null,
    val displayError: Boolean = false
)*/

@Parcelize
@Immutable
internal data class ListState(
    val page: Int = 1,
    val scrollPosition: Int = 0
) : Parcelable
