package com.seancoyle.core.ui.components.videoplayer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens

fun extractYouTubeVideoId(url: String): String? {
    return try {
        when {
            url.contains("youtu.be/") -> {
                url.substringAfter("youtu.be/").substringBefore("?")
            }

            url.contains("youtube.com/watch?v=") -> {
                url.substringAfter("v=").substringBefore("&")
            }

            url.contains("youtube.com/embed/") -> {
                url.substringAfter("embed/").substringBefore("?")
            }

            else -> null
        }
    } catch (e: Exception) {
        null
    }
}

@Composable
fun EmbeddedYouTubePlayer(
    videoId: String,
    videoTitle: String?,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.dp8)
    ) {
        if (!videoTitle.isNullOrEmpty()) {
            AppText.titleMedium(
                text = videoTitle,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = Dimens.dp4)
            )
        }

        AndroidView(
            factory = { context ->
                YouTubePlayerView(context).apply {
                    lifecycleOwner.lifecycle.addObserver(this)

                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(videoId, 0f)
                        }
                    })
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(Dimens.dp12))
        )
    }
}
