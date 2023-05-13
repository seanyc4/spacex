package com.seancoyle.launch.implementation.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seancoyle.launch.api.domain.model.CompanySummary
import com.seancoyle.launch.api.domain.model.SectionTitle
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.implementation.R

@Preview
@Composable
fun PreviewLaunchHeading() {
    val launchHeading = SectionTitle("Test title", ViewType.TYPE_SECTION_TITLE)
    LaunchHeading(launchHeading)
}

@Preview
@Composable
fun PreviewCompanySummaryCard() {
    val companySummary = CompanySummary("Test Summary", ViewType.TYPE_HEADER)
    CompanySummaryCard(companySummary)
}

/*
@Preview
@Composable
fun PreviewLaunchCard() {
    val launchItem = LaunchModel(
        // Fill with appropriate test data
    )
    LaunchCard(launchItem = launchItem, onClick = {})
}
*/

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

/*
@Preview
@Composable
fun PreviewLaunchCarouselCard() {
    val rocketWithMission = RocketWithMission(
        // Fill with appropriate test data
    )
    LaunchCarouselCard(launchItem = rocketWithMission, onClick = {})
}
*/