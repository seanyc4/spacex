package com.seancoyle.feature.launch.presentation.launch

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.seancoyle.core.test.testags.LaunchesTestTags.LAUNCH_SCREEN
import com.seancoyle.core.ui.components.error.ErrorState
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingXLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
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
import com.seancoyle.feature.launch.presentation.launch.model.LinkType

@Composable
fun LaunchScreen(
    launch: LaunchUI,
    onVideoPlay: (videoId: String, isLive: Boolean) -> Unit,
    onExternalLinkClick: (linkType: String) -> Unit,
    onSectionExpand: (sectionName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(0)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(scrollState)
            .semantics { contentDescription = "Launch details for ${launch.missionName}" }
            .testTag(LAUNCH_SCREEN)
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        LaunchHeroSection(launch = launch)
        Spacer(modifier = Modifier.height(paddingXLarge))

        LaunchDetailsSection(launch = launch)

        if (launch.vidUrls.isNotEmpty()) {
            Spacer(modifier = Modifier.height(paddingXLarge))
            VideoSection(
                videos = launch.vidUrls,
                onVideoPlay = onVideoPlay
            )
        }

        if (launch.updates.isNotEmpty()) {
            Spacer(modifier = Modifier.height(paddingXLarge))
            UpdatesSection(
                updates = launch.updates,
                onSectionExpand = { onSectionExpand("updates") }
            )
            Spacer(modifier = Modifier.height(paddingLarge))
        }

        Spacer(modifier = Modifier.height(paddingXLarge))
        LaunchSiteSection(
            pad = launch.pad,
            onExternalLinkClick = { onExternalLinkClick(LinkType.MAP.type) }
        )

        Spacer(modifier = Modifier.height(paddingXLarge))
        RocketSection(
            rocket = launch.rocket,
            onExternalLinkClick = { onExternalLinkClick(LinkType.WEB.type) },
        )

        if (launch.launchServiceProvider != null) {
            Spacer(modifier = Modifier.height(paddingXLarge))
            LaunchProviderSection(agency = launch.launchServiceProvider)
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun LaunchScreenSuccessPreview() {
    AppTheme {
        LaunchScreen(
            launch = previewData(),
            onVideoPlay = { _, _ -> },
            onExternalLinkClick = { },
            onSectionExpand = { }
        )
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
