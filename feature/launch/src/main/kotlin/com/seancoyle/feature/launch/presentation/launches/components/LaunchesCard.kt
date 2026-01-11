package com.seancoyle.feature.launch.presentation.launches.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.components.image.RemoteImage
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.launchCardHeight
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launch.model.containerColor
import com.seancoyle.feature.launch.presentation.launch.model.contentColor
import com.seancoyle.feature.launch.presentation.launch.model.icon
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi

@Composable
internal fun LaunchCard(
    launchItem: LaunchesUi,
    onClick: (String, LaunchesType) -> Unit,
    launchesType: LaunchesType,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val launchCardDesc = stringResource(
        R.string.mission_card_desc_full,
        launchItem.missionName,
        launchItem.status.label,
        launchItem.launchDate
    )
    val missionNameDesc = stringResource(R.string.mission_name_desc, launchItem.missionName)
    val dateTimeDescription = stringResource(R.string.launch_date_desc, launchItem.launchDate)

    val borderStroke = if (isSelected) {
        BorderStroke(3.dp, AppTheme.colors.primary)
    } else {
        null
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(launchCardHeight)
            .clickable { onClick(launchItem.id, launchesType) }
            .semantics {
                contentDescription = launchCardDesc
                selected = isSelected
            }
            .testTag(LaunchesTestTags.LAUNCH_CARD),
        shape = RoundedCornerShape(cornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp,
            pressedElevation = 4.dp
        ),
        border = borderStroke
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            RemoteImage(
                modifier = Modifier.fillMaxSize(),
                imageUrl = launchItem.imageUrl,
                contentDescription = stringResource(R.string.launch_image),
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                AppTheme.colors.surface.copy(alpha = 0.8f),
                                AppTheme.colors.surface.copy(alpha = 0.7f)
                            ),
                            startY = 200f,
                            endY = Float.POSITIVE_INFINITY
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
                        containerColor = launchItem.status.containerColor(),
                        contentColor = launchItem.status.contentColor(),
                        icon = launchItem.status.icon(),
                        modifier = Modifier.testTag(LaunchesTestTags.CARD_STATUS_CHIP)
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
                        maxLines = 1,
                        modifier = Modifier.semantics {
                            contentDescription = missionNameDesc
                        },
                        
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
                            },
                            
                        )
                    }
                }
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun LaunchCardPreview() {
    AppTheme {
        LaunchCard(
            launchItem = LaunchesUi(
                id = "1",
                missionName = "Starlink Mission",
                launchDate = "11 January 2026",
                status = LaunchStatus.SUCCESS,
                imageUrl = ""
            ),
            onClick = { _, _ -> },
            launchesType = LaunchesType.UPCOMING,
            isSelected = true
        )
    }
}
