package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.seancoyle.core.ui.components.progress.CircularProgressBar
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.LaunchesTestTags

@Composable
internal fun LoadingState(modifier: Modifier = Modifier) {
    val loadingDescription = stringResource(R.string.loading_launch_details)

    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                contentDescription = loadingDescription
                testTag = LaunchesTestTags.LOADING_STATE
            },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressBar()
    }
}

@PreviewDarkLightMode
@Composable
private fun LoadingStatePreview() {
    AppTheme {
        LoadingState()
    }
}
