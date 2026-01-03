package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.feature.launch.R

@Composable
internal fun LaunchDetailsSection(
    launch: LaunchUI,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = stringResource(R.string.mission_details))

            // Mission Information - Prominent Card
            launch.mission?.let { mission ->
                MissionHighlightCard(
                    missionName = mission.name,
                    missionType = mission.type,
                    orbitName = mission.orbit?.name,
                    description = mission.description
                )
            }

            // Launch Windows Section
            val hasWindowInfo = launch.windowStart != null || launch.windowEnd != null
            if (hasWindowInfo) {
                HorizontalDivider(
                    color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
                )

                LaunchWindowTimeline(
                    windowStartTime = launch.windowStartTime,
                    windowEndTime = launch.windowEndTime,
                    windowDuration = launch.windowDuration,
                    launchDate = launch.launchDate
                )
            }

            // Fail Reason (if applicable)
            if (!launch.failReason.isNullOrEmpty()) {
                HorizontalDivider(
                    color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
                )

                FailReasonCard(reason = launch.failReason)
            }
        }
    }
}

@Composable
private fun MissionHighlightCard(
    missionName: String?,
    missionType: String?,
    orbitName: String?,
    description: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(Dimens.dp16)
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
                    .padding(Dimens.dp20),
                verticalArrangement = Arrangement.spacedBy(Dimens.dp12)
            ) {
                // Mission Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AppText.labelMedium(
                            text = stringResource(R.string.mission).uppercase(),
                            color = AppTheme.colors.primary,
                            fontWeight = FontWeight.Bold
                        )
                        missionName?.let { name ->
                            AppText.titleLarge(
                                text = name,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.onSurface,
                                modifier = Modifier.padding(top = Dimens.dp4)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AppTheme.colors.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(48.dp)
                    )
                }

                // Mission Type & Orbit Chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.dp8),
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
                            icon = Icons.Default.Star,
                            contentColor = AppTheme.colors.secondary,
                            containerColor = AppTheme.colors.secondary
                        )
                    }
                }

                // Mission Description
                description?.let { desc ->
                    AppText.bodyMedium(
                        text = desc,
                        color = AppTheme.colors.onSurfaceVariant,
                        modifier = Modifier.padding(top = Dimens.dp4)
                    )
                }
            }
        }
    }
}

@Composable
private fun LaunchWindowTimeline(
    windowStartTime: String?,
    windowEndTime: String?,
    windowDuration: String?,
    launchDate: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.dp16)
    ) {
        AppText.titleMedium(
            text = stringResource(R.string.launch_window),
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.onSurface
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimens.dp12)
        ) {
            launchDate?.let { date ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.dp4)
                ) {
                    AppText.titleSmall(
                        text = date,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.dp8)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.dp8)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(
                                color = AppTheme.colors.surfaceVariant.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f) // TODO: Calculate actual progress
                                .fillMaxHeight()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            AppTheme.colors.primary.copy(alpha = 0.6f),
                                            AppTheme.colors.primary
                                        )
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f) // TODO: Calculate actual position
                                .fillMaxHeight()
                                .wrapContentWidth(Alignment.End)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .offset(y = (-8).dp)
                                    .background(
                                        color = AppTheme.colors.primary,
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                                    .padding(2.dp)
                                    .background(
                                        color = AppTheme.colors.surface,
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                            )
                        }
                    }

                    // Time labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        windowStartTime?.let { start ->
                            Column(horizontalAlignment = Alignment.Start) {
                                AppText.labelSmall(
                                    text = stringResource(R.string.window_opens),
                                    color = AppTheme.colors.onSurfaceVariant
                                )
                                AppText.bodyLarge(
                                    text = start,
                                    color = AppTheme.colors.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        windowEndTime?.let { end ->
                            Column(horizontalAlignment = Alignment.End) {
                                AppText.labelSmall(
                                    text = stringResource(R.string.window_closes),
                                    color = AppTheme.colors.onSurfaceVariant
                                )
                                AppText.bodyLarge(
                                    text = end,
                                    color = AppTheme.colors.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Duration card (if duration is available)
            windowDuration?.let { duration ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.colors.primary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(Dimens.dp12)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.dp12),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = AppTheme.colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimens.dp8))
                        AppText.labelLarge(
                            text = stringResource(R.string.duration),
                            color = AppTheme.colors.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(Dimens.dp4))
                        AppText.titleXSmall(
                            text = duration,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary
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
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.error.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(Dimens.dp12)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.dp16),
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp12),
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
                verticalArrangement = Arrangement.spacedBy(Dimens.dp4)
            ) {
                AppText.titleSmall(
                    text = stringResource(R.string.failure_reason),
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.error
                )
                AppText.bodyMedium(
                    text = reason,
                    color = AppTheme.colors.onSurface
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
