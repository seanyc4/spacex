package com.seancoyle.core_ui.extensions

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.adaptiveHorizontalPadding(windowSize: WindowSizeClass, horizontalPadding: Dp = 128.dp): Modifier = this.then(
    if (windowSize.heightSizeClass == WindowHeightSizeClass.Compact) {
        padding(horizontal = horizontalPadding)
    } else {
        this
    }
)
