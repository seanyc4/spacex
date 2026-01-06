package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.buttons.ButtonPrimary
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.LaunchesTestTags

@Composable
internal fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    val errorDescription = stringResource(R.string.error_loading_launch, message)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics {
                contentDescription = errorDescription
                testTag = LaunchesTestTags.ERROR_STATE
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
        ) {
            Icon(
                imageVector = Icons.Default.RocketLaunch,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = AppTheme.colors.error
            )
            AppText.titleLarge(
                text = stringResource(R.string.unable_to_load),
                color = AppTheme.colors.onBackground
            )
            AppText.bodyMedium(
                text = message,
                color = AppTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(paddingMedium))
            ButtonPrimary(
                text = stringResource(R.string.retry),
                onClick = onRetry,
                modifier = Modifier.semantics {
                    testTag = LaunchesTestTags.RETRY_BUTTON
                }
            )

        }
    }
}

@PreviewDarkLightMode
@Composable
private fun ErrorStatePreview() {
    AppTheme {
        ErrorState(
            message = "Network connection error. Please try again.",
            onRetry = {}
        )
    }
}
