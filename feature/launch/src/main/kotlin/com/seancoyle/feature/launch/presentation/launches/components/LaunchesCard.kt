package com.seancoyle.feature.launch.presentation.launches.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import com.seancoyle.feature.launch.presentation.launches.LaunchesTestTags
import com.seancoyle.feature.launch.R
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.presentation.launch.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents

@Composable
internal fun LaunchCard(
    launchItem: LaunchesUi,
    onEvent: (LaunchesEvents) -> Unit,
    onClick: (String, LaunchesType) -> Unit,
    launchesType: LaunchesType,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {}
            .height(Dimens.launchCardHeight)
            .clickable { onClick(launchItem.id, launchesType) },
        shape = RoundedCornerShape(Dimens.dp10),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .semantics { testTag = LaunchesTestTags.LAUNCH_CARD }
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth(0.33f)
                    .fillMaxHeight()
            ) {
                LaunchCardImage(imageUrl = launchItem.image)
            }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(Dimens.dp8)
            ) {

                AppText.titleXSmall(
                    text = launchItem.missionName,
                    modifier = modifier.padding(bottom = Dimens.dp4),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = stringResource(id = R.string.date_time),
                        tint = AppTheme.colors.secondary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(Dimens.dp4))
                    AppText.bodyMedium(
                        text = launchItem.launchDate,
                        color = AppTheme.colors.secondary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Pill(
                        text = launchItem.status.abbrev,
                        color = launchItem.status.pillColor
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
            .fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@PreviewDarkLightMode
@Composable
private fun LaunchCardPreview() {
    AppTheme {
        LaunchCard(
            launchItem = LaunchesUi(
                id = "1",
                missionName = "Starlink Mission",
                launchDate = "2024-01-15",
                status = LaunchStatus.SUCCESS,
                image = ""
            ),
            onEvent = {},
            onClick = { _, _ -> },
            launchesType = LaunchesType.UPCOMING
        )
    }
}
