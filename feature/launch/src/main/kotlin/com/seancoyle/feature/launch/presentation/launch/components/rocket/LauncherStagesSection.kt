package com.seancoyle.feature.launch.presentation.launch.components.rocket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.components.image.RemoteImage
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppColors
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingLarge
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.domain.model.Launcher
import com.seancoyle.feature.launch.domain.model.LauncherStage
import com.seancoyle.feature.launch.presentation.launch.components.SectionTitle
import com.seancoyle.feature.launch.presentation.launch.model.LandingStateUI

@Composable
internal fun LauncherStagesSection(
    stages: List<LauncherStage>,
    modifier: Modifier = Modifier
) {
    AppCard.Primary(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(text = stringResource(R.string.booster_stages))
            AppText.labelMedium(
                text = "${stages.size} ${
                    if (stages.size == 1) stringResource(R.string.stage) else stringResource(
                        R.string.stages
                    )
                }",
                color = AppTheme.colors.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        stages.forEachIndexed { index, stage ->
            if (index > 0) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = paddingLarge),
                    color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
                )
            }
            LauncherStageItem(stage = stage, index = index + 1)
        }
    }
}

@Composable
internal fun LauncherStageItem(
    stage: LauncherStage,
    index: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingLarge),
            verticalAlignment = Alignment.Top
        ) {
            RemoteImage(
                imageUrl = stage.launcher?.image?.thumbnailUrl!!,
                contentDescription = stringResource(
                    R.string.launcher_desc,
                    stage.launcher.serialNumber.orEmpty()
                ),
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(cornerRadiusMedium))
                    .background(AppTheme.colors.surfaceVariant)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppText.titleMedium(
                    text = stringResource(
                        R.string.stage_number,
                        index,
                        stage.type ?: stringResource(R.string.core)
                    ),
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.primary
                )

                stage.launcher.serialNumber?.let { serial ->
                    AppText.bodyMedium(
                        text = stringResource(R.string.serial, serial),
                        color = AppTheme.colors.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                stage.reused?.let { isReused ->
                    Spacer(modifier = Modifier.height(paddingSmall))
                    Chip(
                        text = if (isReused) stringResource(R.string.reused) else stringResource(R.string.new_booster),
                        contentColor = if (isReused) AppColors.success else AppTheme.colors.primary,
                        containerColor = if (isReused) AppColors.success else AppTheme.colors.primary
                    )
                }
            }
        }

        stage.launcher?.let { launcher ->
            LauncherStats(launcher = launcher)
        }

        stage.landing?.let { landing ->
            LandingInfo(
                attempt = landing.attempt,
                success = landing.success,
                description = landing.description,
                locationName = landing.location?.name,
                typeName = landing.type?.name
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            stage.launcherFlightNumber?.let {
                InfoChip(
                    label = stringResource(R.string.flight_number),
                    value = it.toString(),
                    icon = Icons.Default.Info
                )
            }

            stage.launcher?.flights?.let {
                InfoChip(
                    label = stringResource(R.string.total_flights),
                    value = it.toString(),
                    icon = Icons.Default.Star
                )
            }
        }
    }
}

@Composable
internal fun LauncherStats(
    launcher: Launcher,
    modifier: Modifier = Modifier
) {
    AppCard.Subtle(modifier = modifier.fillMaxWidth()) {
        AppText.labelMedium(
            text = stringResource(R.string.launcher_performance),
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colors.secondary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            launcher.successfulLandings?.let { successful ->
                launcher.attemptedLandings?.let { attempted ->
                    MiniStatItem(
                        value = "$successful/$attempted",
                        label = stringResource(R.string.landings),
                        icon = Icons.Default.CheckCircle,
                        iconTint = AppColors.success
                    )
                }
            }

            launcher.flightProven?.let { proven ->
                MiniStatItem(
                    value = if (proven) stringResource(R.string.yes) else stringResource(R.string.no),
                    label = stringResource(R.string.flight_proven),
                    icon = Icons.Default.Star,
                    iconTint = if (proven) AppColors.warning else AppTheme.colors.secondary
                )
            }
        }

        launcher.status?.name?.let { status ->
            AppText.bodySmall(
                text = stringResource(R.string.status_label, status),
                color = AppTheme.colors.secondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
internal fun LandingInfo(
    attempt: Boolean?,
    success: Boolean?,
    description: String?,
    locationName: String?,
    typeName: String?,
    modifier: Modifier = Modifier
) {
    val (containerColor, iconVector, iconTint, labelResId) = resolveLandingState(attempt, success)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(cornerRadiusMedium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingLarge),
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppText.labelLarge(
                    text = stringResource(labelResId),
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.primary
                )

                locationName?.let {
                    AppText.bodySmall(
                        text = stringResource(R.string.location_label, it),
                        color = AppTheme.colors.secondary
                    )
                }

                typeName?.let {
                    AppText.bodySmall(
                        text = stringResource(R.string.type_label, it),
                        color = AppTheme.colors.secondary
                    )
                }

                description?.let {
                    AppText.bodySmall(
                        text = it,
                        color = AppTheme.colors.secondary,
                        modifier = Modifier.padding(top = paddingSmall)
                    )
                }
            }
        }
    }
}

@Composable
private fun resolveLandingState(attempt: Boolean?, success: Boolean?): LandingStateUI {
    return when {
        success == true -> LandingStateUI(
            containerColor = AppColors.success.copy(alpha = 0.1f),
            iconVector = Icons.Default.CheckCircle,
            iconTint = AppColors.success,
            labelResId = R.string.landing_successful
        )

        attempt == true && success == false -> LandingStateUI(
            containerColor = AppColors.error.copy(alpha = 0.1f),
            iconVector = Icons.Default.Warning,
            iconTint = AppColors.error,
            labelResId = R.string.landing_failed
        )

        attempt == true -> LandingStateUI(
            containerColor = AppTheme.colors.surfaceVariant.copy(alpha = 0.3f),
            iconVector = Icons.Default.Info,
            iconTint = AppTheme.colors.secondary,
            labelResId = R.string.landing_attempted
        )

        else -> LandingStateUI(
            containerColor = AppTheme.colors.surfaceVariant.copy(alpha = 0.3f),
            iconVector = Icons.Default.Info,
            iconTint = AppTheme.colors.secondary,
            labelResId = R.string.no_landing_attempt
        )
    }
}
