package com.seancoyle.feature.launch.presentation.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingXLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.presentation.launch.components.ErrorState
import com.seancoyle.feature.launch.presentation.launch.components.LaunchDetailsSection
import com.seancoyle.feature.launch.presentation.launch.components.LaunchHeroSection
import com.seancoyle.feature.launch.presentation.launch.components.LaunchProviderSection
import com.seancoyle.feature.launch.presentation.launch.components.LaunchSiteSection
import com.seancoyle.feature.launch.presentation.launch.components.LoadingState
import com.seancoyle.feature.launch.presentation.launch.components.RocketSection
import com.seancoyle.feature.launch.presentation.launch.components.UpdatesSection
import com.seancoyle.feature.launch.presentation.launch.components.VideoSection
import com.seancoyle.feature.launch.presentation.launch.components.previewData
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.feature.launch.presentation.launch.state.LaunchEvent
import com.seancoyle.feature.launch.presentation.launch.state.LaunchUiState

@Composable
fun LaunchScreen(
    viewModel: LaunchViewModel,
    @Suppress("UNUSED_PARAMETER") snackbarHostState: SnackbarHostState,
    @Suppress("UNUSED_PARAMETER") windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    val launchState by viewModel.launchState.collectAsStateWithLifecycle()

    LaunchScreenContent(
        launchState = launchState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
private fun LaunchScreenContent(
    launchState: LaunchUiState,
    onEvent: (LaunchEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .testTag("launch_screen")
    ) {
        when (launchState) {
            is LaunchUiState.Loading -> LoadingState()
            is LaunchUiState.Success -> SuccessState(launch = launchState.launch)
            is LaunchUiState.Error -> ErrorState(
                message = launchState.message,
                onRetry = { onEvent(LaunchEvent.RetryFetch) }
            )
        }
    }
}

@Composable
private fun SuccessState(
    launch: LaunchUI,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = "Launch details for ${launch.missionName}" },
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { LaunchHeroSection(launch = launch) }
        item { Spacer(modifier = Modifier.height(paddingXLarge)) }

        item { LaunchDetailsSection(launch = launch) }

        item { Spacer(modifier = Modifier.height(paddingXLarge)) }
        item { LaunchSiteSection(pad = launch.pad) }

        if (launch.vidUrls.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(paddingXLarge)) }
            item { VideoSection(videos = launch.vidUrls) }
        }

        item { Spacer(modifier = Modifier.height(paddingXLarge)) }
        item { RocketSection(rocket = launch.rocket) }

        if (launch.launchServiceProvider != null) {
            item { Spacer(modifier = Modifier.height(paddingXLarge)) }
            item { LaunchProviderSection(agency = launch.launchServiceProvider) }
        }

        if (launch.updates.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(paddingXLarge)) }
            item { UpdatesSection(updates = launch.updates) }
        }
    }
}


@PreviewDarkLightMode
@Composable
private fun LaunchScreenSuccessPreview() {
    AppTheme {
        SuccessState(launch = previewData())
    }
}

@PreviewDarkLightMode
@Composable
private fun LaunchScreenLoadingPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background)
        ) {
            LoadingState()
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun LaunchScreenErrorPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background)
        ) {
            ErrorState(
                message = "Unable to connect to server. Please check your internet connection.",
                onRetry = {}
            )
        }
    }
}
