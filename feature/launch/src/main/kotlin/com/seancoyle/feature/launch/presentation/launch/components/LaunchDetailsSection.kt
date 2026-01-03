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
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import java.time.LocalDateTime

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
            // Show timeline only if we have window information (not future launches without windows)
            val hasWindowInfo = launch.windowStart != null && launch.windowEnd != null
            if (hasWindowInfo) {
                HorizontalDivider(color = AppTheme.colors.onSurface.copy(alpha = 0.12f))
                LaunchWindowTimeline(
                    windowStartTime = launch.windowStartTime,
                    windowEndTime = launch.windowEndTime,
                    windowDuration = launch.windowDuration,
                    windowStartDateTime = launch.windowStartDateTime,
                    windowEndDateTime = launch.windowEndDateTime,
                    launchTime = launch.launchTime,
                    launchDateTime = launch.launchDateTime,
                    launchDate = launch.launchDate,
                    status = launch.status,
                    modifier = Modifier.padding(Dimens.dp16)
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
                            AppText.titleMedium(
                                text = name,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.onSurface,
                                modifier = Modifier.padding(top = Dimens.dp4)
                            )
                        }
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
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AppTheme.colors.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
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
    windowStartDateTime: LocalDateTime?,
    windowEndDateTime: LocalDateTime?,
    launchTime: String?,
    launchDateTime: LocalDateTime?,
    launchDate: String?,
    status: LaunchStatus,
    modifier: Modifier = Modifier
) {
    // Calculate timeline progress based on actual launch time position
    val launchProgress =
        calculateLaunchPosition(windowStartDateTime, windowEndDateTime, launchDateTime)

    // Check if this is an instantaneous launch (same start and end time) OR status is TBD
    val isInstantaneous = windowDuration == "Instantaneous"
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.primary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(Dimens.dp12)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.dp20),
            verticalArrangement = Arrangement.spacedBy(Dimens.dp12)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AppText.labelMedium(
                        text = stringResource(R.string.launch_window).uppercase(),
                        color = AppTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                    AppText.titleMedium(
                        text = launchDate.orEmpty(),
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.onSurface,
                        modifier = Modifier.padding(top = Dimens.dp4)
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
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.dp6)
            ) {
                launchTime?.let { time ->
                    AppText.headlineLarge(
                        text = time,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Timeline - only show if NOT instantaneous
            if (!isInstantaneous) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.dp8)
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
                                        .fillMaxWidth(launchProgress)
                                        .fillMaxHeight()
                                        .wrapContentWidth(Alignment.End)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .align(Alignment.CenterEnd)
                                            .background(
                                                color = AppTheme.colors.tertiary,
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
                        }

                        // Time labels - just the times, no labels (only show if NOT instantaneous)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            windowStartTime?.let { start ->
                                AppText.bodyLarge(
                                    text = start,
                                    color = AppTheme.colors.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            windowEndTime?.let { end ->
                                AppText.bodyLarge(
                                    text = end,
                                    color = AppTheme.colors.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            } else {
                // For instantaneous launches, show only background track (no active portion, no times)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.dp16, horizontal = 20.dp)
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
                windowDuration?.let { duration ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.dp8),
                        colors = CardDefaults.cardColors(
                            containerColor = AppTheme.colors.primary.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(Dimens.dp10)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Dimens.dp12),
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
                                color = AppTheme.colors.onSurfaceVariant,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            AppText.titleXSmall(
                                text = duration,
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
}

/**
 * Calculates the position of the launch time within the launch window.
 * Returns a value between 0.0 and 1.0 representing the position in the window.
 */
private fun calculateLaunchPosition(
    windowStart: LocalDateTime?,
    windowEnd: LocalDateTime?,
    launchTime: LocalDateTime?
): Float {
    if (windowStart == null || windowEnd == null || launchTime == null) return 0.5f

    // If launch time is before window, show at start
    if (launchTime.isBefore(windowStart)) return 0f

    // If launch time is after window, show at end
    if (launchTime.isAfter(windowEnd)) return 1f

    // Calculate position of launch time within the window
    val totalDuration = java.time.Duration.between(windowStart, windowEnd).toMillis().toFloat()
    val launchOffset = java.time.Duration.between(windowStart, launchTime).toMillis().toFloat()

    // Return position between 0 and 1
    return (launchOffset / totalDuration).coerceIn(0f, 1f)
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
