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
    val transition = rememberInfiniteTransition()
    val animatedValue = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing)
        )
    )

    val gradientWidth = 800
    val shimmerColor = colors[1].copy(alpha = 0.2f)
    val shimmer = Brush.linearGradient(
        colors = colors + shimmerColor,
        start = Offset(
            x = -gradientWidth.toFloat(),
            y = 0f
        ),
        end = Offset(
            x = animatedValue.value * gradientWidth,
            y = animatedValue.value * gradientWidth
        )
    )

    content(shimmer)
}