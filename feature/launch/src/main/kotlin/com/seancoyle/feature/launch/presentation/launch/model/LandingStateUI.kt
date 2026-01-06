package com.seancoyle.feature.launch.presentation.launch.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class to hold landing state UI properties.
 */
data class LandingStateUI(
    val containerColor: Color,
    val iconVector: ImageVector,
    val iconTint: Color,
    val labelResId: Int
)
