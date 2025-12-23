package com.seancoyle.core.ui.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Immutable
data class AppTheme(
    val color: Color = Color.Unspecified,
    val tonalElevation: Dp = Dp.Unspecified,
)

val LocalAppTheme = staticCompositionLocalOf { AppTheme() }