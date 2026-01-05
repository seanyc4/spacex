package com.seancoyle.core.ui.components.image

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusLarge

@Composable
fun RemoteImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = 1.0f,
    colorFilter: ColorFilter? = null,
    failureImage: Int = R.drawable.default_launch_image,
    loadingImage: Int = R.drawable.rocket_loading_icon
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .crossfade(500)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCacheKey(imageUrl)
            .build(),
        contentDescription = contentDescription,
       // placeholder = painterResource(loadingImage),
        error = painterResource(failureImage),
        fallback = painterResource(failureImage),
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadiusLarge)),
        contentScale = contentScale,
        alignment = alignment,
        alpha = alpha,
        colorFilter = colorFilter
    )
}
