package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seancoyle.feature.launch.implementation.R

@Preview
@Composable
fun PreviewLaunchCardImage() {
    LaunchCardImage(
        imageUrl = "https://via.placeholder.com/150",
    )
}

@Preview
@Composable
fun PreviewLaunchCardDefaultText() {
    LaunchCardDefaultText(title = R.string.app_name)
}

@Preview
@Composable
fun PreviewLaunchCardDynamicText() {
    LaunchCardDynamicText(title = "Test title")
}
