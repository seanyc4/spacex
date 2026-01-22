package com.seancoyle.feature.launch.presentation.launch

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.ui.components.error.ErrorState
import com.seancoyle.core.ui.designsystem.pulltorefresh.RefreshableContent
import com.seancoyle.feature.launch.presentation.launch.components.LoadingState
import com.seancoyle.feature.launch.presentation.launch.state.LaunchEvent
import com.seancoyle.feature.launch.presentation.launch.state.LaunchUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchRoute(
    viewModel: LaunchViewModel,
    modifier: Modifier = Modifier,
) {
    val launchState by viewModel.launchState.collectAsStateWithLifecycle()

    when (val state = launchState) {
        is LaunchUiState.Loading -> {
            LoadingState()
        }

        is LaunchUiState.Success -> {
            RefreshableContent(
                isRefreshing = false,
                onRefresh = { viewModel.onEvent(LaunchEvent.PullToRefresh) },
                content ={
                LaunchScreen(
                    launch = state.launch,
                    modifier = modifier
                )
            })
        }

        is LaunchUiState.Error -> {
            ErrorState(
                onRetry = { viewModel.onEvent(LaunchEvent.Retry) }
            )
        }
    }
}
