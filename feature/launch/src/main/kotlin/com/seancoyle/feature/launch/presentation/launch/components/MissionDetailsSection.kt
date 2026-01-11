package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingXLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI

@Composable
internal fun LaunchDetailsSection(
    launch: LaunchUI,
    modifier: Modifier = Modifier
) {
    AppCard.Primary(modifier = modifier) {
        SectionTitle(text = stringResource(R.string.mission_details))

        MissionHighlightCard(
            missionName = launch.mission.name,
            missionType = launch.mission.type,
            orbitName = launch.mission.orbitName,
            description = launch.mission.description
        )

        HorizontalDivider(color = AppTheme.colors.onSurface.copy(alpha = 0.12f))

        LaunchWindowTimeline(
            launchDate = launch.launchDate,
            launchTime = launch.launchTime,
            windowStartTime = launch.windowStartTime,
            windowEndTime = launch.windowEndTime,
            windowDuration = launch.windowDuration,
            launchWindowPosition = launch.launchWindowPosition,
            status = launch.status,
            modifier = Modifier.padding(paddingXLarge)
        )

        if (!launch.failReason.isNullOrEmpty()) {
            HorizontalDivider(color = AppTheme.colors.onSurface.copy(alpha = 0.12f))
            FailReasonCard(reason = launch.failReason)
        }
    }
}

@Composable
private fun MissionHighlightCard(
    missionName: String,
    missionType: String?,
    orbitName: String?,
    description: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(cornerRadiusLarge)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            AppTheme.colors.primary.copy(alpha = 0.15f),
                            AppTheme.colors.secondary.copy(alpha = 0.15f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingMedium)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AppText.labelMedium(
                            text = stringResource(R.string.mission).uppercase(),
                            color = AppTheme.colors.primary,
                            fontWeight = FontWeight.Bold,
                            
                        )

                        AppText.titleMedium(
                            text = missionName,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.onSurface,
                            modifier = Modifier.padding(top = paddingSmall)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = AppTheme.colors.primary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(cornerRadiusSmall)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AppTheme.colors.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(paddingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    missionType?.let { type ->
                        Chip(
                            text = type,
                            icon = Icons.Default.Build,
                            contentColor = AppTheme.colors.primary,
                            containerColor = AppTheme.colors.primary
                        )
                    }

                    orbitName?.let { orbit ->
                        Chip(
                            text = orbit,
                            icon = Icons.Default.MyLocation,
                            contentColor = AppTheme.colors.secondary,
                            containerColor = AppTheme.colors.secondary
                        )
                    }
                }

                AppText.bodyMedium(
                    text = description ?: stringResource(R.string.description_not_available),
                    color = AppTheme.colors.secondary,
                    modifier = Modifier.padding(top = paddingSmall)
                )
            }
        }
    }
}

@Composable
private fun LaunchWindowTimeline(
    launchDate: String,
    launchTime: String,
    windowStartTime: String,
    windowEndTime: String,
    windowDuration: String,
    launchWindowPosition: Float,
    status: LaunchStatus,
    modifier: Modifier = Modifier
) {
    // Check if this is an instantaneous launch
    val isInstantaneous = windowDuration == "Instantaneous"

    AppCard.Tinted(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                AppText.labelMedium(
                    text = stringResource(R.string.launch_window).uppercase(),
                    color = AppTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    
                )

                AppText.titleMedium(
                    text = launchDate,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.onSurface,
                    modifier = Modifier.padding(top = paddingSmall)
                )
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = AppTheme.colors.primary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Rocket,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingSmall)
            ) {
                AppText.headlineLarge(
                    text = launchTime,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.primary,
                    textAlign = TextAlign.Center
                )
            }

            // Timeline - only show if NOT instantaneous
            if (!isInstantaneous) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = paddingMedium)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .padding(horizontal = 20.dp)
                        ) {
                            // Background track (darker/shadow effect) - full width
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .align(Alignment.Center)
                                    .background(
                                        color = AppTheme.colors.scrim.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )

                            // Active portion (gradient) - inset horizontally
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .align(Alignment.Center)
                                    .padding(horizontal = 48.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    AppTheme.colors.primary.copy(alpha = 0.7f),
                                                    AppTheme.colors.primary
                                                )
                                            ),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )

                                // Launch time indicator circle - positioned within active track
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(launchWindowPosition)
                                        .fillMaxHeight()
                                        .wrapContentWidth(Alignment.End)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .align(Alignment.CenterEnd)
                                            .background(
                                                color = AppTheme.colors.tertiary,
                                                shape = CircleShape
                                            )
                                            .padding(2.dp)
                                            .background(
                                                color = AppTheme.colors.surface,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                        }

                        // Time labels - just the times, no labels (only show if NOT instantaneous)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                                AppText.bodyLarge(
                                    text = windowStartTime,
                                    color = AppTheme.colors.secondary,
                                    fontWeight = FontWeight.Medium
                                )

                                AppText.bodyLarge(
                                    text = windowEndTime,
                                    color = AppTheme.colors.secondary,
                                    fontWeight = FontWeight.Medium
                                )
                        }
                    }
                }
            } else {
                // For instantaneous launches, show only background track (no active portion, no times)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = paddingXLarge, horizontal = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(
                                color = AppTheme.colors.scrim.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            if (status != LaunchStatus.TBD) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = paddingMedium),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.colors.primary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(cornerRadiusSmall)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(paddingLarge),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = AppTheme.colors.primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        AppText.bodyMedium(
                            text = stringResource(R.string.duration),
                            color = AppTheme.colors.secondary,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        AppText.titleXSmall(
                            text = windowDuration,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun FailReasonCard(
    reason: String,
    modifier: Modifier = Modifier
) {
    AppCard.Error(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingXLarge),
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingMedium),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = AppTheme.colors.error,
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppText.titleSmall(
                    text = stringResource(R.string.failure_reason),
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.error,
                    
                )
                AppText.bodyMedium(
                    text = reason,
                    color = AppTheme.colors.onSurface,
                )
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun LaunchDetailsSectionPreview() {
    AppTheme {
        LaunchDetailsSection(
            launch = previewData()
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun MissionHighlightCardPreview() {
    AppTheme {
        MissionHighlightCard(
            missionName = "Starlink Group 7-12",
            missionType = "Communications",
            orbitName = "Low Earth Orbit",
            description = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system."
        )
    }
}
