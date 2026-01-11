package com.seancoyle.orbital.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.rocketIconSize
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.navigation.R

@Composable
fun PlaceholderDetailScreen(
    modifier: Modifier = Modifier,
    message: Int = R.string.select_item_placeholder
) {
    val messageText = stringResource(message)

    val infiniteTransition = rememberInfiniteTransition(label = "placeholderAnimation")

    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -18f, // Moves up 12dp
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatingOffset"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f, // Scales to 120%
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingScale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f, // Fades from 80% to 100% opacity
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .semantics {
                contentDescription = messageText
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.RocketLaunch,
            contentDescription = stringResource(R.string.rocket_icon_desc),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(rocketIconSize)
                .offset(y = floatingOffset.dp) // Vertical floating motion
                .scale(scale)
                .alpha(alpha)
        )

        Spacer(modifier = modifier.height(28.dp))

        AppText.titleLarge(
            text = messageText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            isSelectable = false
        )
    }
}

@PreviewDarkLightMode
@Composable
fun DetailPlaceholderPreview() {
    AppTheme {
        PlaceholderDetailScreen()
    }
}
