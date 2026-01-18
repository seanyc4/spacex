package com.seancoyle.feature.launch.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.components.SectionTitle
import com.seancoyle.feature.launch.presentation.launch.model.VidUrlUI
import org.junit.Rule
import org.junit.Test

class VideoSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenVideos_whenVideoSectionDisplayed_thenShowsSectionTitle() {
        val videos = listOf(createTestVideo())

        composeRule.setContent {
            AppTheme {
                TestVideoSection(videos = videos)
            }
        }

        composeRule.onNodeWithText("Videos & Webcasts")
            .assertIsDisplayed()
    }

    @Test
    fun givenVideoWithTitle_whenVideoSectionDisplayed_thenShowsVideoTitle() {
        val videos = listOf(createTestVideo(title = "Launch Webcast"))

        composeRule.setContent {
            AppTheme {
                TestVideoSection(videos = videos)
            }
        }

        composeRule.onNodeWithText("Launch Webcast")
            .assertIsDisplayed()
    }

    @Test
    fun givenVideoWithPublisher_whenVideoSectionDisplayed_thenShowsPublisher() {
        val videos = listOf(createTestVideo(publisher = "SpaceX"))

        composeRule.setContent {
            AppTheme {
                TestVideoSection(videos = videos)
            }
        }

        composeRule.onNodeWithText("SpaceX")
            .assertIsDisplayed()
    }

    @Test
    fun givenLiveVideo_whenVideoSectionDisplayed_thenShowsLiveChip() {
        val videos = listOf(createTestVideo(isLive = true))

        composeRule.setContent {
            AppTheme {
                TestVideoSection(videos = videos)
            }
        }

        composeRule.onNodeWithText("LIVE")
            .assertIsDisplayed()
    }

    @Test
    fun givenNonLiveVideo_whenVideoSectionDisplayed_thenHidesLiveChip() {
        val videos = listOf(createTestVideo(isLive = false))

        composeRule.setContent {
            AppTheme {
                TestVideoSection(videos = videos)
            }
        }

        composeRule.onNodeWithText("LIVE")
            .assertDoesNotExist()
    }

    @Test
    fun givenMultipleVideos_whenVideoSectionDisplayed_thenShowsFirstVideo() {
        val videos = listOf(
            createTestVideo(title = "Video One"),
            createTestVideo(title = "Video Two")
        )

        composeRule.setContent {
            AppTheme {
                TestVideoSection(videos = videos)
            }
        }

        composeRule.onNodeWithText("Video One")
            .assertIsDisplayed()
    }

    @Test
    fun givenSingleVideo_whenVideoSectionDisplayed_thenHidesPagerIndicator() {
        val videos = listOf(createTestVideo())

        composeRule.setContent {
            AppTheme {
                TestVideoSection(videos = videos)
            }
        }

        composeRule.onNodeWithText("Videos & Webcasts")
            .assertIsDisplayed()
    }

    private fun createTestVideo(
        title: String = "Test Video",
        url: String = "https://youtube.com/watch?v=test123",
        isLive: Boolean = false,
        videoId: String = "test123",
        publisher: String = "Test Publisher"
    ): VidUrlUI = VidUrlUI(
        title = title,
        url = url,
        isLive = isLive,
        videoId = videoId,
        publisher = publisher
    )
}

@Composable
private fun TestVideoSection(
    videos: List<VidUrlUI>,
    modifier: Modifier = Modifier
) {
    AppCard.Primary(modifier = modifier) {
        SectionTitle(text = stringResource(R.string.videos_webcasts))

        videos.firstOrNull()?.let { video ->
            TestVideoContent(video = video)
        }
    }
}

@Composable
private fun TestVideoContent(
    video: VidUrlUI,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppText.titleSmall(
            text = video.title,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colors.onSurface,
            maxLines = 2
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText.bodySmall(
                text = video.publisher,
                color = AppTheme.colors.secondary,
                modifier = Modifier.weight(1f, fill = false)
            )

            if (video.isLive) {
                Chip(
                    text = stringResource(R.string.live),
                    contentColor = AppTheme.colors.error,
                    containerColor = AppTheme.colors.error
                )
            }
        }
    }
}
