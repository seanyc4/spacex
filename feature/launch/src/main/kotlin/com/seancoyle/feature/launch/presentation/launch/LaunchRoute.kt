package com.seancoyle.feature.launch.presentation.launch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.feature.launch.presentation.launch.components.ErrorState
import com.seancoyle.feature.launch.presentation.launch.components.LoadingState
import com.seancoyle.feature.launch.presentation.launch.state.LaunchEvent
import com.seancoyle.feature.launch.presentation.launch.state.LaunchUiState

@Composable
fun LaunchRoute(
    viewModel: LaunchViewModel,
    modifier: Modifier = Modifier,
) {
    val launchState by viewModel.launchState.collectAsStateWithLifecycle()

    when (val state = launchState) {
        is LaunchUiState.Loading ->  {
            LoadingState()
        }

        is LaunchUiState.Success -> {
            LaunchScreen(
                launch = state.launch,
                modifier = modifier)
        }

        is LaunchUiState.Error -> {
            ErrorState(
                message = state.message,
                onRetry = { viewModel.onEvent(LaunchEvent.RetryFetch)}
            )
        }
    }
}
