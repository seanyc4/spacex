package com.seancoyle.feature.launch.implementation.presentation.state

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUi
import kotlinx.parcelize.Parcelize

internal sealed interface LaunchesUiState {
    @Stable
    data class Success(
        val launches: List<LaunchUi>
    ) : LaunchesUiState

    data object Loading : LaunchesUiState

    data class Error(
        val errorNotificationState: NotificationState? = null
    ) : LaunchesUiState
}

@Parcelize
@Stable
internal data class LaunchesScreenState(
    val query: String = "",
    val order: Order = Order.ASC,
    val launchStatus: LaunchStatus = LaunchStatus.ALL,
    val isFilterDialogVisible: Boolean = false,
    val scrollPosition: Int = 0
) : Parcelable
