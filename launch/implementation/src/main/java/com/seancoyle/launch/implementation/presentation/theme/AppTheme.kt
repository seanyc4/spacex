package com.seancoyle.launch.implementation.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@ExperimentalMaterialApi
@Composable
fun AppTheme(
    darkTheme: Boolean,
    displayProgressBar: Boolean,
    scaffoldState: ScaffoldState,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = LightThemeColors
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = primaryColor)
        ){
            content()
        }
    }
}

private val LightThemeColors = lightColors(
    primary = primaryColor,
    primaryVariant = primaryColor,
    onPrimary = primaryColor,
    secondary = secondaryColor,
    secondaryVariant = secondaryColor,
    onSecondary = secondaryColor,
    error = errorColor,
    onError = errorColor,
    background = primaryColor,
    onBackground = primaryColor,
    surface = secondaryColor,
    onSurface = secondaryColor,
)