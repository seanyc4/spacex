package com.seancoyle.core.ui.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalPalette = compositionLocalOf { lightPalette }

data class AppPalette(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val inversePrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceTint: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color,
    val surfaceBright: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainerLowest: Color,
    val surfaceDim: Color,
    val isLight: Boolean
) {

    val colourScheme: ColorScheme by lazy {
        if (isLight) {
            lightColorScheme(
                primary = primary,
                onPrimary = onPrimary,
                primaryContainer = primaryContainer,
                onPrimaryContainer = onPrimaryContainer,
                inversePrimary = inversePrimary,
                secondary = secondary,
                onSecondary = onSecondary,
                secondaryContainer = secondaryContainer,
                onSecondaryContainer = onSecondaryContainer,
                tertiary = tertiary,
                onTertiary = onTertiary,
                tertiaryContainer = tertiaryContainer,
                onTertiaryContainer = onTertiaryContainer,
                background = background,
                onBackground = onBackground,
                surface = surface,
                onSurface = onSurface,
                surfaceVariant = surfaceVariant,
                onSurfaceVariant = onSurfaceVariant,
                surfaceTint = surfaceTint,
                inverseSurface = inverseSurface,
                inverseOnSurface = inverseOnSurface,
                error = error,
                onError = onError,
                errorContainer = errorContainer,
                onErrorContainer = onErrorContainer,
                outline = outline,
                outlineVariant = outlineVariant,
                scrim = scrim,
                surfaceBright = surfaceBright,
                surfaceContainer = surfaceContainer,
                surfaceContainerHigh = surfaceContainerHigh,
                surfaceContainerHighest = surfaceContainerHighest,
                surfaceContainerLow = surfaceContainerLow,
                surfaceContainerLowest = surfaceContainerLowest,
                surfaceDim = surfaceDim
            )
        } else {
            darkColorScheme(
                primary = primary,
                onPrimary = onPrimary,
                primaryContainer = primaryContainer,
                onPrimaryContainer = onPrimaryContainer,
                inversePrimary = inversePrimary,
                secondary = secondary,
                onSecondary = onSecondary,
                secondaryContainer = secondaryContainer,
                onSecondaryContainer = onSecondaryContainer,
                tertiary = tertiary,
                onTertiary = onTertiary,
                tertiaryContainer = tertiaryContainer,
                onTertiaryContainer = onTertiaryContainer,
                background = background,
                onBackground = onBackground,
                surface = surface,
                onSurface = onSurface,
                surfaceVariant = surfaceVariant,
                onSurfaceVariant = onSurfaceVariant,
                surfaceTint = surfaceTint,
                inverseSurface = inverseSurface,
                inverseOnSurface = inverseOnSurface,
                error = error,
                onError = onError,
                errorContainer = errorContainer,
                onErrorContainer = onErrorContainer,
                outline = outline,
                outlineVariant = outlineVariant,
                scrim = scrim,
                surfaceBright = surfaceBright,
                surfaceContainer = surfaceContainer,
                surfaceContainerHigh = surfaceContainerHigh,
                surfaceContainerHighest = surfaceContainerHighest,
                surfaceContainerLow = surfaceContainerLow,
                surfaceContainerLowest = surfaceContainerLowest,
                surfaceDim = surfaceDim
            )
        }
    }
}

val lightPalette = AppPalette(
    primary = Color(0xFF006493),
    onPrimary = Color(0xFFF0F0F0),
    primaryContainer = Color(0xFFCAE6FF),
    onPrimaryContainer = Color(0xFF001E30),
    inversePrimary = Color(0xFF8DCDFF),
    secondary = Color(0xFF50606E),
    onSecondary = Color(0xFFF0F0F0),
    secondaryContainer = Color(0xFFD3E5F5),
    onSecondaryContainer = Color(0xFF0C1D29),
    tertiary = Color(0xFF9C432E),
    onTertiary = Color(0xFFF0F0F0),
    tertiaryContainer = Color(0xFFFFDAD2),
    onTertiaryContainer = Color(0xFF3D0700),
    background = Color(0xFFDCE9F5),        // Very blue steel background
    onBackground = Color(0xFF001F25),
    surface = Color(0xFFD0E1F0),           // Strong blue-grey surface
    onSurface = Color(0xFF001F25),
    surfaceVariant = Color(0xFFC5D8E8),    // Blue-grey (appCard.tinted)
    onSurfaceVariant = Color(0xFF41474D),
    surfaceTint = Color(0xFF006493),
    inverseSurface = Color(0xFF00363F),
    inverseOnSurface = Color(0xFFD6F6FF),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFF0F0F0),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFF72787E),
    outlineVariant = Color(0xFFC1C7CE),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFFE8F2FA),     // Very light blue
    surfaceContainer = Color(0xFFDCEBF4),  // Lighter blue steel (appCard primary)
    surfaceContainerHigh = Color(0xFFBDD5E5),    // Medium blue-grey
    surfaceContainerHighest = Color(0xFFB0CDDF), // Deep blue-grey (appCard.subtle)
    surfaceContainerLow = Color(0xFFDBE8F3),     // Light blue-grey
    surfaceContainerLowest = Color(0xFFEDF4F9),  // Very light blue
    surfaceDim = Color(0xFFAFC6D6),        // Dimmed blue-grey
    isLight = true
)

val darkPalette = AppPalette(
    primary = Color(0xFF8DCDFF),
    onPrimary = Color(0xFF00344F),
    primaryContainer = Color(0xFF004B70),
    onPrimaryContainer = Color(0xFFCAE6FF),
    inversePrimary = Color(0xFF006493),
    secondary = Color(0xFFD3E5F5),
    onSecondary = Color(0xFF22323F),
    secondaryContainer = Color(0xFF384956),
    onSecondaryContainer = Color(0xFFD3E5F5),
    tertiary = Color(0xFFFFB4A3),
    onTertiary = Color(0xFF5E1606),
    tertiaryContainer = Color(0xFF7D2C19),
    onTertiaryContainer = Color(0xFFFFDAD2),
    background = Color(0xFF212121),
    onBackground = Color(0xFFA6EEFF),
    surface = Color(0xFF424242),
    onSurface = Color(0xFFA6EEFF),
    surfaceVariant = Color(0xFF41474D),
    onSurfaceVariant = Color(0xFFC1C7CE),
    surfaceTint = Color(0xFF8DCDFF),
    inverseSurface = Color(0xFFA6EEFF),
    inverseOnSurface = Color(0xFF001F25),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF8B9198),
    outlineVariant = Color(0xFF41474D),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFF535C66),
    surfaceContainer = Color(0xFF353535),
    surfaceContainerHigh = Color(0xFF353535),
    surfaceContainerHighest = Color(0xFF353535),
    surfaceContainerLow = Color(0xFF353535),
    surfaceContainerLowest = Color(0xFF353535),
    surfaceDim = Color(0xFF424242),
    isLight = false
)
