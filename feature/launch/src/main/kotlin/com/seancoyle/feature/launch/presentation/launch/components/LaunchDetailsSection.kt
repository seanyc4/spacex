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
import androidx.compose.ui.graphics.vector.ImageVector
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

                AppText.titleMedium(
                    text = "Launch Window",
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.dp12)
                ) {
                    launch.windowStart?.let { windowStart ->
                        TimeCard(
                            label = stringResource(R.string.window_opens),
                            time = windowStart,
                            icon = Icons.Default.DateRange,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    launch.windowEnd?.let { windowEnd ->
                        TimeCard(
                            label = stringResource(R.string.window_closes),
                            time = windowEnd,
                            icon = Icons.Default.DateRange,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
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
private fun TimeCard(
    label: String,
    time: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(Dimens.dp12)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.dp16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.dp8)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )
            AppText.labelSmall(
                text = label,
                color = AppTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            AppText.bodyMedium(
                text = time,
                color = AppTheme.colors.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
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
