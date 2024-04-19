package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import com.seancoyle.feature.launch.api.domain.model.LinkType
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchBottomSheetCard
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchBottomSheetExitButton

@ExperimentalMaterialApi
@ExperimentalMaterial3WindowSizeClassApi
@Composable
internal fun LaunchBottomSheetScreen(
    linkTypes: List<LinkType>?,
    dismiss: () -> Unit,
    windowSize: WindowSizeClass
) {
    Column {
        LaunchBottomSheetCard(
            linkTypes = linkTypes,
            windowSize = windowSize
        )
        LaunchBottomSheetExitButton(
            windowSize = windowSize,
            actionExitClicked = { dismiss() }
        )
    }
}