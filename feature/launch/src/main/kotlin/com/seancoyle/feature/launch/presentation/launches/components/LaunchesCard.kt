package com.seancoyle.feature.launch.presentation.launches.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.LaunchesTestTags
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents

@Composable
internal fun LaunchCard(
    launchItem: LaunchesUi,
    @Suppress("UNUSED_PARAMETER") onEvent: (LaunchesEvents) -> Unit,
    onClick: (String, LaunchesType) -> Unit,
    launchesType: LaunchesType,
    modifier: Modifier = Modifier,
) {
    val missionNameDesc = stringResource(R.string.mission_name_desc, launchItem.missionName)
    val dateTimeDescription = stringResource(R.string.launch_date_desc, launchItem.launchDate)
    val launchCardDesc = stringResource(R.string.mission_card_desc, launchItem.missionName)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick(launchItem.id, launchesType) }
            .semantics {
                testTag = LaunchesTestTags.LAUNCH_CARD
                contentDescription = launchCardDesc
            },
        shape = RoundedCornerShape(cornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LaunchCardImage(
                imageUrl = launchItem.imageUrl,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                AppTheme.colors.surface.copy(alpha = 0.9f),
                                AppTheme.colors.surface.copy(alpha = 1f)
                            ),
                            startY = 0f,
                            endY = 800f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingLarge),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Chip(
                        text = launchItem.status.abbrev,
                        containerColor = launchItem.status.containerColor,
                        contentColor = launchItem.status.contentColor,
                        icon = launchItem.status.icon,
                        modifier = Modifier.semantics {
                            testTag = LaunchesTestTags.FILTER_STATUS_CHIP
                        }
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingSmall)
                ) {
                    AppText.titleMedium(
                        text = launchItem.missionName,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.primary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        modifier = Modifier.semantics {
                            contentDescription = missionNameDesc
                        }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingSmall)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = stringResource(R.string.date_time),
                            tint = AppTheme.colors.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        AppText.bodyMedium(
                            text = launchItem.launchDate,
                            color = AppTheme.colors.secondary,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.semantics {
                                contentDescription = dateTimeDescription
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun LaunchCardImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    GlideImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.launch_image),
        failure = placeholder(R.drawable.default_mission_thumbnail),
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadiusLarge))
            .semantics { testTag = LaunchesTestTags.LAUNCH_IMAGE },
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
                imageUrl = ""
            ),
            onEvent = {},
            onClick = { _, _ -> },
            launchesType = LaunchesType.UPCOMING
        )
    }
}
