package com.seancoyle.core.ui.components.videoplayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingSmall

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
    modifier: Modifier = Modifier,
    videoTitle: String = "Video player",
    onVideoStarted: () -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val accessibilityDescription = "YouTube video: $videoTitle. Double tap to play or pause."

    Column(
        modifier = modifier.semantics {
            contentDescription = accessibilityDescription
        },
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingSmall)
    ) {
        AndroidView(
            factory = { context ->
                YouTubePlayerView(context).apply {
                    lifecycleOwner.lifecycle.addObserver(this)

                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(videoId, 0f)
                        }

                        override fun onStateChange(
                            youTubePlayer: YouTubePlayer,
                            state: PlayerConstants.PlayerState
                        ) {
                            if (state == PlayerConstants.PlayerState.PLAYING) {
                                onVideoStarted()
                            }
                        }
                    })
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(cornerRadiusMedium))
        )
    }
}
