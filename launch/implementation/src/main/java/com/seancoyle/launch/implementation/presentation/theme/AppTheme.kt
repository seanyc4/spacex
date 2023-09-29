package com.seancoyle.launch.implementation.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@ExperimentalMaterialApi
@Composable
fun AppTheme(
    darkTheme: Boolean,
    displayProgressBar: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = LightThemeColors
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ){
            content()
        }
    }
}

private val LightThemeColors = lightColors(
    primary = primaryColor,
    primaryVariant = primaryVariantColor,
    onPrimary = white,
    secondary = secondaryColor,
    secondaryVariant = secondaryColor,
    onSecondary = white,
    error = errorColor,
    onError = errorColor,
    background = backgroundColor,
    onBackground = white,
    surface = surfaceColor,
    onSurface = white,
)