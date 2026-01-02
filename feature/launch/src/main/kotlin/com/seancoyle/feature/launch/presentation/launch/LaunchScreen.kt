package com.seancoyle.feature.launch.presentation.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.presentation.launch.components.*
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.feature.launch.presentation.launch.state.LaunchEvent
import com.seancoyle.feature.launch.presentation.launch.state.LaunchUiState

@Composable
fun LaunchScreen(
    launchId: String,
    launchesType: LaunchesType,
    @Suppress("UNUSED_PARAMETER") snackbarHostState: SnackbarHostState,
    @Suppress("UNUSED_PARAMETER") windowSizeClass: androidx.compose.material3.windowsizeclass.WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    val viewModel: LaunchViewModel =
        hiltViewModel<LaunchViewModel, LaunchViewModel.Factory> { factory ->
            factory.create(launchId = launchId, launchType = launchesType)
        }

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
        contentPadding = PaddingValues(bottom = Dimens.dp24)
    ) {
        item { LaunchHeroSection(launch = launch) }
        item { Spacer(modifier = Modifier.height(Dimens.dp16)) }

        item { LaunchDetailsSection(launch = launch) }

        if (launch.pad != null) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { LaunchSiteSection(pad = launch.pad) }
        }

        if (launch.vidUrls.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { VideoSection(videos = launch.vidUrls) }
        }

        if (launch.rocket != null) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { RocketSection(rocket = launch.rocket) }
        }

        if (launch.missionPatches.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { MissionPatchesSection(patches = launch.missionPatches) }
        }

        if (launch.launchServiceProvider != null) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { AgencySection(agency = launch.launchServiceProvider) }
        }

        if (launch.updates.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
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
