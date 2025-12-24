package com.seancoyle.core.ui.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorPalette = if (isDarkTheme) darkPalette else lightPalette

    CompositionLocalProvider(
        LocalPalette provides colorPalette,
    ) {
        MaterialTheme(
            colorScheme = colorPalette.colourScheme,
            typography = AppTypography,
            shapes = MaterialTheme.shapes,
            content = content
        )
    }
}