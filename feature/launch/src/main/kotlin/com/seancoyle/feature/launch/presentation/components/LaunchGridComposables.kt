package com.seancoyle.feature.launch.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.seancoyle.core.ui.designsystem.pill.Pill
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.LaunchTestTags
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.model.LaunchUi
import com.seancoyle.feature.launch.presentation.state.LaunchesEvents

@Composable
internal fun LaunchCard(
    launchItem: LaunchUi,
    onEvent: (LaunchesEvents) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {}
            .height(Dimens.launchCardHeight),
        shape = RoundedCornerShape(Dimens.dp10),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .semantics { testTag = LaunchTestTags.LAUNCH_CARD }
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth(0.30f)
            ) {
                LaunchCardImage(imageUrl = launchItem.image)
            }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(Dimens.dp8)
            ) {

                AppText.titleSmall(
                    text = launchItem.missionName,
                    modifier = modifier.padding(bottom = Dimens.dp4),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                AppText.bodyMedium(
                    text = launchItem.launchDate,
                    color = AppTheme.colors.secondary,
                    modifier = modifier.padding(bottom = Dimens.dp6),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Pill(
                        text = launchItem.status.text,
                        color = launchItem.status.color
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun LaunchCardImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    GlideImage(
        model = imageUrl,
        contentDescription = stringResource(id = R.string.launch_image),
        failure = placeholder(R.drawable.default_launch_image),
        modifier = modifier
            .fillMaxSize()
    )
}

@PreviewDarkLightMode
@Composable
private fun LaunchCardPreview() {
    AppTheme {
        LaunchCard(
            launchItem = LaunchUi(
                id = "1",
                missionName = "Starlink Mission",
                launchDate = "2024-01-15",
                status = LaunchStatus.SUCCESS,
                image = ""
            ),
            onEvent = {}
        )
    }
}
