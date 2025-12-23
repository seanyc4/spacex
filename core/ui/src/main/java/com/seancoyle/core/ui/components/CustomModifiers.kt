package com.seancoyle.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.adaptiveHorizontalPadding(
    isLandscape: Boolean,
    horizontalPadding: Dp = 128.dp
): Modifier = this.then(
    if (isLandscape) {
        this.padding(horizontal = horizontalPadding)
    } else {
        this
    }
)