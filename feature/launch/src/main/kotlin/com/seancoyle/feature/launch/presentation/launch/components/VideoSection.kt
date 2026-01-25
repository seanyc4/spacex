package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.components.videoplayer.EmbeddedYouTubePlayer
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingMedium
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.model.VidUrlUI

@Composable
internal fun VideoSection(
    videos: List<VidUrlUI>,
    onVideoPlay: (videoId: String, isLive: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { videos.size })

    AppCard.Primary(modifier = modifier) {
        SectionTitle(text = stringResource(R.string.videos_webcasts))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { role = Role.Carousel }
        ) { page ->
            val video = videos[page]
            VideoContent(
                videoId = video.videoId!!,
                video = video,
                onVideoPlay = { onVideoPlay(video.videoId, video.isLive) }
            )
        }

        if (videos.size > 1) {
            Spacer(modifier = Modifier.height(paddingMedium))
            PagerIndicator(
                pageCount = videos.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun VideoContent(
    videoId: String,
    video: VidUrlUI,
    onVideoPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingMedium)
    ) {
        EmbeddedYouTubePlayer(
            videoId = videoId,
            modifier = Modifier.fillMaxWidth(),
            videoTitle = video.title,
            onVideoStarted = onVideoPlay
        )

        VideoMetadata(video = video)
    }
}

@Composable
private fun PagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    val pageDescription = stringResource(R.string.page_indicator, currentPage + 1, pageCount)

    Row(
        modifier = modifier.clearAndSetSemantics {
            contentDescription = pageDescription
        },
        horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingSmall)
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .size(if (isSelected) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            AppTheme.colors.primary
                        } else {
                            AppTheme.colors.onSurface.copy(alpha = 0.3f)
                        }
                    )
            )
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
        AppText.titleSmall(
            text = video.title,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colors.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingSmall),
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
                    containerColor = AppTheme.colors.error,
                    accessibilityLabel = stringResource(R.string.video_status)
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
            videos = previewData().vidUrls,
            onVideoPlay = { _, _ -> }
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun VideoContentPreview() {
    AppTheme {
        AppCard.Primary {
            VideoContent(
                videoId = "dQw4w9WgXcQ",
                video = previewData().vidUrls.first(),
                onVideoPlay = {}
            )
        }
    }
}
