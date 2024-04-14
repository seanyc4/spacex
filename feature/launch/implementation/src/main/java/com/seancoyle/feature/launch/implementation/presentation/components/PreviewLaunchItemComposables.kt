package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.R

@Preview
@Composable
fun PreviewLaunchHeading() {
    val launchHeading = LaunchTypes.SectionTitle("12345", "Test title")
    LaunchHeading(launchHeading)
}

@Preview
@Composable
fun PreviewLaunchCardImage() {
    LaunchCardImage(
        imageUrl = "https://via.placeholder.com/150", // replace with appropriate test data
        size = 60.dp
    )
}

@Preview
@Composable
fun PreviewLaunchCardDefaultText() {
    LaunchCardDefaultText(title = R.string.app_name) // replace with appropriate test data
}

@Preview
@Composable
fun PreviewLaunchCardDynamicText() {
    LaunchCardDynamicText(title = "Test title")
}