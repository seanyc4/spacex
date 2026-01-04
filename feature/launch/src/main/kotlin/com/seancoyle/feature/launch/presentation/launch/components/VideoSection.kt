package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.domain.model.VidUrl

@Composable
internal fun VideoSection(
    videos: List<VidUrl>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.dp8)
    ) {
        AppText.titleLarge(
            text = "Videos & Webcasts",
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary,
            modifier = Modifier
                .padding(horizontal = Dimens.dp16)
                .semantics { contentDescription = "Section: Videos & Webcasts" }
        )

        Spacer(modifier = Modifier.height(Dimens.dp16))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = Dimens.dp16),
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp16)
        ) {
            items(videos.size) { index ->
                val video = videos[index]
                video.url?.let { url ->
                    val videoId = extractYouTubeVideoId(url)

                    if (videoId != null) {
                        VideoCard(
                            videoId = videoId,
                            video = video
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VideoCard(
    videoId: String,
    video: VidUrl,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(380.dp)
            .height(300.dp)
        ,
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(Dimens.dp16)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.dp16),
            verticalArrangement = Arrangement.spacedBy(Dimens.dp12)
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
    video: VidUrl,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.dp4)
    ) {
        if (!video.title.isNullOrEmpty()) {
            AppText.titleSmall(
                text = video.title,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!video.publisher.isNullOrEmpty()) {
                AppText.bodySmall(
                    text = video.publisher,
                    color = AppTheme.colors.secondary,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }

            if (video.live == true) {
                Chip(
                    text = "LIVE",
                    contentColor = AppTheme.colors.error,
                    containerColor = AppTheme.colors.error
                )
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun VideoSectionPreview() {
    AppTheme {
        VideoSection(
            videos = listOf(
                VidUrl(
                    priority = 1,
                    source = "YouTube",
                    publisher = "SpaceX",
                    title = "Falcon 9 Launch",
                    description = "Live launch coverage",
                    featureImage = null,
                    url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
                    startTime = null,
                    endTime = null,
                    live = true
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
            video = VidUrl(
                priority = 1,
                source = "YouTube",
                publisher = "SpaceX",
                title = "Falcon 9 Launch and Landing",
                description = "Live launch coverage",
                featureImage = null,
                url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
                startTime = null,
                endTime = null,
                live = false
            )
        )
    }
}
