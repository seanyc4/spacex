package com.seancoyle.feature.launch.presentation.launch.components

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.designsystem.buttons.ButtonPrimary
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.rocketIconSize
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

    // Scale animation: grows bigger during takeoff then returns to normal during crash
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 5000
                1f at 0                                   // Start normal
                1.15f at 600                              // Growing during takeoff
                1.25f at 1200                             // Max size at peak
                1.2f at 2000                              // Slight shrink as trouble starts
                1f at 3000                              // Shrinking during fall
                1f at 4000                                // Back to normal
                1f at 5000                                // Hold
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "crashingScale"
    )

    // Rotation animation: takes off, wobbles, then crashes pointing southeast
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 5000
                0f at 0                                   // Start upright
                (-5f) at 400                              // Slight tilt during takeoff
                0f at 800                                 // Straighten out
                10f at 1200                                // Peak - slight wobble right
                30f at 1600                             // Wobble left - something's wrong
                50f at 2000                               // Starting to tip
                65f at 2400                               // Tipping over
                85f at 2800                               // Horizontal
                100f at 3200                              // Nose pointing down
                110f at 3600                              // Crashed - pointing southeast
                110f at 4200                              // Hold crashed position
                0f at 5000                                // Smooth reset
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "crashingRotation"
    )

    // Vertical offset: rises up during takeoff, then falls down during crash
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 5000
                0f at 0                                   // Start position
                (-15f) at 600                             // Rising up - takeoff!
                (-25f) at 1200                            // Peak height
                (-20f) at 1600                            // Starting to fall
                (-10f) at 2000                            // Falling
                5f at 2400                                // Dropping faster
                20f at 2800                               // Accelerating down
                35f at 3200                               // Almost crashed
                45f at 3600                               // Crashed position
                45f at 4200                               // Hold at crash
                0f at 5000                                // Reset
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "crashingOffsetY"
    )

    // Horizontal offset: drifts right as it crashes
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 5000
                0f at 0                                   // Start centered
                0f at 1200                                // Stay centered during takeoff
                5f at 2000                                // Slight drift right
                15f at 2800                               // Drifting more
                25f at 3600                               // Crashed offset
                25f at 4200                               // Hold
                0f at 5000                                // Reset
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "crashingOffsetX"
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
            Spacer(modifier)

            Icon(
                imageVector = Icons.Default.RocketLaunch,
                contentDescription = null,
                modifier = Modifier
                    .size(rocketIconSize)
                    .scale(scale)
                    .graphicsLayer {
                        rotationZ = rotation
                        translationX = offsetX.dp.toPx()
                        translationY = offsetY.dp.toPx()
                    },
                tint = AppTheme.colors.error
            )

            Spacer(modifier.height(16.dp))

            AppText.titleLarge(
                text = stringResource(R.string.unable_to_load),
                color = AppTheme.colors.onBackground,
                
            )
            AppText.bodyMedium(
                text = message,
                color = AppTheme.colors.onSurfaceVariant,
                
            )

            Spacer(modifier)

            ButtonPrimary(
                text = stringResource(R.string.retry),
                onClick = onRetry,
                modifier = Modifier.testTag(LaunchesTestTags.RETRY_BUTTON)
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
