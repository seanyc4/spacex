package com.seancoyle.core.ui.components.progress

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ShimmerAnimation(
    colors: List<Color>,
    content: @Composable (brush: Brush) -> Unit
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val animatedValue = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing)
        ),
        label = "shimmer_offset"
    )

    val gradientWidth = 1200f
    val shimmerColor = colors[1].copy(alpha = 0.2f)

    // Create a repeating gradient pattern for seamless animation
    val repeatPattern = colors + shimmerColor + colors

    // Calculate offset that creates a continuous loop
    val offset = animatedValue.value * gradientWidth * 2

    val shimmer = Brush.linearGradient(
        colors = repeatPattern,
        start = Offset(
            x = offset - gradientWidth,
            y = offset - gradientWidth
        ),
        end = Offset(
            x = offset + gradientWidth,
            y = offset + gradientWidth
        )
    )

    content(shimmer)
}
