package com.seancoyle.core.ui.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.seancoyle.core.ui.R

val orbitronFontFamily = FontFamily(
    Font(R.font.orbitron)
)

val groteskFontFamily = FontFamily(
    Font(R.font.space_grotesk)
)

object AppTextStyles {

    val displayLarge: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = orbitronFontFamily,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp,
            color = AppTheme.colors.primary
        )

    val displayMedium: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = orbitronFontFamily,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp,
            color = AppTheme.colors.primary
        )

    val displaySmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = orbitronFontFamily,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp,
            color = AppTheme.colors.primary
        )

    val headlineLarge: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontFamily = orbitronFontFamily,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp,
            color = AppTheme.colors.primary
        )

    val headlineMedium: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontFamily = orbitronFontFamily,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp,
            color = AppTheme.colors.primary
        )

    val headlineSmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontFamily = orbitronFontFamily,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
            color = AppTheme.colors.primary,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Bottom,
                trim = LineHeightStyle.Trim.None,
            ),
        )

    val titleLarge: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontFamily = orbitronFontFamily,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.2.sp,
            color = AppTheme.colors.primary,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Bottom,
                trim = LineHeightStyle.Trim.LastLineBottom,
            ),
        )

    val titleMedium: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontFamily = orbitronFontFamily,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.2.sp,
            color = AppTheme.colors.primary
        )

    val titleSmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontFamily = orbitronFontFamily,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.2.sp,
            color = AppTheme.colors.primary
        )

    val titleXSmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontFamily = orbitronFontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.2.sp,
            color = AppTheme.colors.primary
        )

    val bodyLarge: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = groteskFontFamily,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
            color = AppTheme.colors.secondary,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
        )

    val bodyMedium: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = groteskFontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp,
            color = AppTheme.colors.secondary
        )

    val bodySmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = groteskFontFamily,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp,
            color = AppTheme.colors.secondary
        )

    val labelLarge: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = groteskFontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
            color = AppTheme.colors.primary
        )

    val labelMedium: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.Medium,
            fontFamily = groteskFontFamily,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
            color = AppTheme.colors.primary
        )

    val labelSmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontWeight = FontWeight.Medium,
            fontFamily = groteskFontFamily,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
            color = AppTheme.colors.primary
        )
}
