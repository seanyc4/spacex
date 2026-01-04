package com.seancoyle.core.ui.components.image

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusLarge

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RemoteImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: Int,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = 1f,
    colorFilter: ColorFilter? = null,
    failureImage: Int = R.drawable.default_launch_image,
    loadingImage: Int = R.drawable.default_launch_image
) {
    GlideImage(
        model = imageUrl,
        contentDescription = stringResource(contentDescription),
        failure = placeholder(failureImage),
        loading = placeholder(loadingImage),
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadiusLarge)),
        contentScale = contentScale,
        alignment = alignment,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}
