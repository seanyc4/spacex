package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.designsystem.buttons.ButtonPrimary
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R

@Composable
internal fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    val errorDescription = stringResource(R.string.error_loading_launch, message)

    val infiniteTransition = rememberInfiniteTransition(label = "errorRocketAnimation")

    // Scale animation: grows bigger then returns to normal
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                1f at 0 using FastOutSlowInEasing        // Start normal
                1.3f at 800 using FastOutSlowInEasing    // Grow bigger
                1.3f at 1200 using FastOutSlowInEasing   // Hold at max size
                1f at 2000 using FastOutSlowInEasing     // Shrink back
                1f at 3000                                // Hold at normal
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "crashingScale"
    )

    // Rotation animation: tips over like crashing
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0 using FastOutSlowInEasing        // Start upright
                0f at 800 using FastOutSlowInEasing      // Still upright while growing
                (-10f) at 1000 using FastOutSlowInEasing   // Wobble left
                10f at 1200 using FastOutSlowInEasing    // Wobble right
                45f at 1600 using FastOutSlowInEasing    // Tip over to the right
                60f at 2000 using FastOutSlowInEasing    // Crash angle
                60f at 2400                               // Hold crashed
                0f at 3000 using FastOutSlowInEasing     // Reset to upright
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "crashingRotation"
    )

    // Vertical offset: drops down when crashing
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0 using FastOutSlowInEasing        // Start position
                (-8f) at 800 using FastOutSlowInEasing     // Rise up slightly while growing
                (-8f) at 1200 using FastOutSlowInEasing    // Hold up
                12f at 2000 using FastOutSlowInEasing    // Drop down (crash)
                12f at 2400                               // Hold at crash position
                0f at 3000 using FastOutSlowInEasing     // Reset
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "crashingOffsetY"
    )

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
                modifier = Modifier
                    .size(64.dp)
                    .scale(scale)
                    .graphicsLayer {
                        rotationZ = rotation
                        translationY = offsetY.dp.toPx()
                    },
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
