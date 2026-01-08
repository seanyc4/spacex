package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.components.videoplayer.EmbeddedYouTubePlayer
import com.seancoyle.core.ui.components.videoplayer.extractYouTubeVideoId
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingXLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingMedium
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.model.VidUrlUI

/**
 * Data class to hold pre-computed video data to avoid repeated computation during recomposition.
 */
private data class VideoData(
    val videoId: String,
    val video: VidUrlUI
)

@Composable
internal fun VideoSection(
    videos: List<VidUrlUI>,
    modifier: Modifier = Modifier
) {
    val titleDescription =
        stringResource(R.string.section_desc, stringResource(R.string.videos_webcasts))

    // Pre-compute video IDs once to avoid repeated extraction during recomposition
    val videoDataList = remember(videos) {
        videos.mapNotNull { video ->
            extractYouTubeVideoId(video.url)?.let { videoId ->
                VideoData(videoId = videoId, video = video)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = paddingMedium)
    ) {
        AppText.titleLarge(
            text = stringResource(R.string.videos_webcasts),
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary,
            modifier = Modifier
                .padding(horizontal = paddingXLarge)
                .semantics { contentDescription = titleDescription }
        )

        Spacer(modifier = Modifier.height(paddingXLarge))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = paddingXLarge),
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingLarge)
        ) {
            items(
                items = videoDataList,
                key = { it.videoId }
            ) { videoData ->
                VideoCard(
                    videoId = videoData.videoId,
                    video = videoData.video
                )
            }
        }
    }
}

@Composable
private fun VideoCard(
    videoId: String,
    video: VidUrlUI,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(380.dp)
            .height(300.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(cornerRadiusLarge)
    ) {
        Column(
            modifier = Modifier.padding(paddingXLarge),
            verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingMedium)
        ) {
            EmbeddedYouTubePlayer(
                videoId = videoId,
                videoTitle = null,
                modifier = Modifier.fillMaxWidth(),
                showTitle = false
            )

            VideoMetadata(video = video)
        }
    }
}

@Composable
private fun VideoMetadata(
    video: VidUrlUI,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (video.title.isNotEmpty() && video.title != NA) {
            AppText.titleSmall(
                text = video.title,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (video.publisher.isNotEmpty() && video.publisher != NA) {
                AppText.bodySmall(
                    text = video.publisher,
                    color = AppTheme.colors.secondary,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }

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

private const val NA = "N/A"

@PreviewDarkLightMode
@Composable
private fun VideoSectionPreview() {
    AppTheme {
        VideoSection(
            videos = listOf(
                VidUrlUI(
                    title = "Falcon 9 Launch",
                    description = "Live launch coverage",
                    url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
                    thumbnailUrl = "",
                    publisher = "SpaceX",
                    isLive = true
                )
            )
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun VideoCardPreview() {
    AppTheme {
        VideoCard(
            videoId = "dQw4w9WgXcQ",
            video = VidUrlUI(
                title = "Falcon 9 Launch and Landing",
                description = "Live launch coverage",
                url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnailUrl = "",
                publisher = "SpaceX",
                isLive = false
            )
        )
    }
}
