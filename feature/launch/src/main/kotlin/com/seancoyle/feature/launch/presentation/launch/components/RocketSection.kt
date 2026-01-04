package com.seancoyle.feature.launch.presentation.launch.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.RocketLaunch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTextStyles
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusXSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingXLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.domain.model.Configuration
import com.seancoyle.feature.launch.domain.model.Family
import com.seancoyle.feature.launch.domain.model.Launcher
import com.seancoyle.feature.launch.domain.model.LauncherStage
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.domain.model.SpacecraftStage

@Composable
internal fun RocketSection(
    rocket: Rocket,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
    ) {
        RocketConfigurationCard(config = rocket.configuration)

        if (!rocket.launcherStage.isNullOrEmpty()) {
            LauncherStagesSection(stages = rocket.launcherStage)
        }

        if (!rocket.spacecraftStage.isNullOrEmpty()) {
            SpacecraftStagesSection(stages = rocket.spacecraftStage)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RocketConfigurationCard(
    config: Configuration,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)) {
            SectionTitle(text = stringResource(R.string.rocket_config))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.colors.primary.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(cornerRadiusMedium)
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
                                text = stringResource(R.string.rocket).uppercase(),
                                color = AppTheme.colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                            AppText.titleMedium(
                                text = config.fullName ?: config.name ?: "Unknown Rocket",
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.onSurface,
                                modifier = Modifier.padding(top = paddingSmall)
                            )
                            if (!config.variant.isNullOrEmpty()) {
                                AppText.bodyLarge(
                                    text = config.variant,
                                    color = AppTheme.colors.secondary,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(top = paddingSmall)
                                )
                            }

                            if (!config.alias.isNullOrEmpty()) {
                                AppText.bodyMedium(
                                    text = stringResource(R.string.also_known_as, config.alias),
                                    color = AppTheme.colors.secondary
                                )
                            }
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
                                imageVector = Icons.Default.RocketLaunch,
                                contentDescription = null,
                                tint = AppTheme.colors.primary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    config.image?.imageUrl?.let { imageUrl ->
                        GlideImage(
                            model = imageUrl,
                            contentDescription = stringResource(R.string.rocket_desc, config.name.orEmpty()),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(cornerRadiusMedium)),
                            failure = placeholder(R.drawable.default_launch_hero_image)
                        )
                    }

                    config.description?.let { desc ->
                        AppText.bodyMedium(
                            text = desc,
                            color = AppTheme.colors.secondary,
                            modifier = Modifier.padding(vertical = paddingMedium)
                        )
                    }
                }
            }

            HorizontalDivider(
                color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
            )

            AppText.titleMedium(
                text = stringResource(R.string.launch_statistics),
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingMedium)
            ) {
                config.totalLaunchCount?.let {
                    StatItem(
                        value = it.toString(),
                        label = stringResource(R.string.total_launches),
                        icon = Icons.Default.Star,
                        modifier = Modifier.weight(1f)
                    )
                }

                config.failedLaunches?.let {
                    if (it > 0) {
                        StatItem(
                            value = it.toString(),
                            label = stringResource(R.string.failed),
                            icon = Icons.Default.Warning,
                            valueColor = Color(0xFFF44336),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                config.successfulLaunches?.let {
                    StatItem(
                        value = it.toString(),
                        label = stringResource(R.string.successful),
                        icon = Icons.Default.CheckCircle,
                        valueColor = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            HorizontalDivider(
                color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
            )

            AppText.titleMedium(
                text = stringResource(R.string.physical_specifications),
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary
            )

            Column(verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingMedium)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingMedium)
                ) {
                    config.length?.let { length ->
                        PhysicalSpecItem(
                            label = stringResource(R.string.length),
                            value = String.format("%.1f m", length),
                            icon = Icons.Default.ArrowUpward,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    config.diameter?.let { diameter ->
                        PhysicalSpecItem(
                            label = stringResource(R.string.diameter),
                            value = String.format("%.1f m", diameter),
                            icon = Icons.Default.Info,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                config.launchMass?.let { mass ->
                    PhysicalSpecItem(
                        label = stringResource(R.string.launch_mass),
                        value = String.format("%.0f kg", mass),
                        icon = Icons.Default.Build
                    )
                }
            }

            HorizontalDivider(
                color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
            )

            AppText.titleMedium(
                text = stringResource(R.string.manufacturer_and_history),
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary
            )

            Column(verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingMedium)) {
                config.manufacturer?.let { manufacturer ->
                    DetailRow(
                        label = stringResource(R.string.manufacturer),
                        value = manufacturer.name,
                        icon = Icons.Default.Build
                    )

                    manufacturer.country?.firstOrNull()?.name?.let { country ->
                        DetailRow(
                            label = stringResource(R.string.built_in),
                            value = country,
                            icon = Icons.Default.Place
                        )
                    }

                    manufacturer.foundingYear?.let { year ->
                        DetailRow(
                            label = stringResource(R.string.company_founded),
                            value = year.toString(),
                            icon = Icons.Default.DateRange
                        )
                    }
                }

                config.maidenFlight?.let { maidenFlight ->
                    DetailRow(
                        label = stringResource(R.string.maiden_flight),
                        value = maidenFlight,
                        icon = Icons.Default.Star
                    )
                }
            }

            if (!config.families.isNullOrEmpty()) {
                HorizontalDivider(
                    color = AppTheme.colors.primary.copy(alpha = 0.1f)
                )

                AppText.titleMedium(
                    text = stringResource(R.string.rocket_family),
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.primary
                )

                config.families.forEach { family ->
                    RocketFamilyCard(family = family)
                }
            }

            // External Links
            val context = LocalContext.current
            val hasLinks = config.wikiUrl != null || config.manufacturer?.wikiUrl != null

            if (hasLinks) {
                HorizontalDivider(
                    color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
                )

                AppText.titleMedium(
                    text = stringResource(R.string.learn_more),
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.primary
                )

                Column(verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingSmall)) {
                    config.wikiUrl?.let { wikiUrl ->
                        LinkButton(
                            text = stringResource(R.string.rocket_on_wikipedia),
                            icon = Icons.Default.Info,
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, wikiUrl.toUri())
                                context.startActivity(intent)
                            }
                        )
                    }

                    config.manufacturer?.wikiUrl?.let { wikiUrl ->
                        LinkButton(
                            text = stringResource(R.string.manufacturer_on_wikipedia),
                            icon = Icons.Default.Build,
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, wikiUrl.toUri())
                                context.startActivity(intent)
                            }
                        )
                    }

                    config.manufacturer?.infoUrl?.let { infoUrl ->
                        LinkButton(
                            text = stringResource(R.string.manufacturer_website),
                            icon = Icons.Default.AccountCircle,
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, infoUrl.toUri())
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RocketFamilyCard(
    family: Family,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(cornerRadiusMedium)
    ) {
        Column(
            modifier = Modifier.padding(paddingXLarge),
            verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingSmall)
        ) {
            family.name?.let { name ->
                AppText.titleSmall(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.primary
                )
            }

            family.description?.let { desc ->
                AppText.bodySmall(
                    text = desc,
                    color = AppTheme.colors.secondary
                )
            }

            family.maidenFlight?.let { maidenFlight ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AppTheme.colors.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                    AppText.bodySmall(
                        text = stringResource(R.string.maiden_flight_date, maidenFlight),
                        color = AppTheme.colors.secondary
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = paddingMedium),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                family.totalLaunchCount?.let {
                    MiniStatItem(
                        value = it.toString(),
                        label = stringResource(R.string.launches),
                        icon = Icons.Default.Star
                    )
                }

                family.successfulLaunches?.let {
                    MiniStatItem(
                        value = it.toString(),
                        label = stringResource(R.string.successful),
                        icon = Icons.Default.CheckCircle,
                        iconTint = Color(0xFF4CAF50)
                    )
                }

                family.active?.let { isActive ->
                    MiniStatItem(
                        value = if (isActive) stringResource(R.string.yes) else stringResource(R.string.no),
                        label = stringResource(R.string.active),
                        icon = Icons.Default.CheckCircle,
                        iconTint = if (isActive) Color(0xFF4CAF50) else AppTheme.colors.secondary
                    )
                }
            }
        }
    }
}

@Composable
private fun PhysicalSpecItem(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(cornerRadiusXSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.onSurface,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                AppText.bodySmall(
                    text = label,
                    color = AppTheme.colors.secondary,
                    fontSize = AppTextStyles.labelSmall.fontSize
                )
                AppText.titleSmall(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.onSurface
                )
            }
        }
    }
}

@Composable
private fun LauncherStagesSection(
    stages: List<LauncherStage>,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(text = stringResource(R.string.booster_stages))
                AppText.labelMedium(
                    text = "${stages.size} ${if (stages.size == 1) stringResource(R.string.stage) else stringResource(R.string.stages)}",
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
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LauncherStageItem(
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
            stage.launcher?.image?.thumbnailUrl?.let { imageUrl ->
                GlideImage(
                    model = imageUrl,
                    contentDescription = stringResource(R.string.launcher_desc, stage.launcher.serialNumber.orEmpty()),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(cornerRadiusMedium))
                        .background(AppTheme.colors.surfaceVariant),
                    failure = placeholder(R.drawable.default_launch_hero_image)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppText.titleMedium(
                    text = stringResource(R.string.stage_number, index, stage.type ?: stringResource(R.string.core)),
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.primary
                )

                stage.launcher?.serialNumber?.let { serial ->
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
                        contentColor = if (isReused) Color(0xFF4CAF50) else AppTheme.colors.primary,
                        containerColor = if (isReused) Color(0xFF4CAF50) else AppTheme.colors.primary
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
private fun LauncherStats(
    launcher: Launcher,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(cornerRadiusMedium)
    ) {
        Column(
            modifier = Modifier.padding(paddingLarge),
            verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingSmall)
        ) {
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
                            iconTint = Color(0xFF4CAF50)
                        )
                    }
                }

                launcher.flightProven?.let { proven ->
                    MiniStatItem(
                        value = if (proven) stringResource(R.string.yes) else stringResource(R.string.no),
                        label = stringResource(R.string.flight_proven),
                        icon = Icons.Default.Star,
                        iconTint = if (proven) Color(0xFFFFC107) else AppTheme.colors.secondary
                    )
                }
            }

            launcher.status?.name?.let { status ->
                AppText.bodySmall(
                    text = "Status: $status",
                    color = AppTheme.colors.secondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun LandingInfo(
    attempt: Boolean?,
    success: Boolean?,
    description: String?,
    locationName: String?,
    typeName: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                success == true -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                attempt == true && success == false -> Color(0xFFF44336).copy(alpha = 0.1f)
                else -> AppTheme.colors.surfaceVariant.copy(alpha = 0.3f)
            }
        ),
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
                imageVector = when {
                    success == true -> Icons.Default.CheckCircle
                    attempt == true && success == false -> Icons.Default.Warning
                    else -> Icons.Default.Info
                },
                contentDescription = null,
                tint = when {
                    success == true -> Color(0xFF4CAF50)
                    attempt == true && success == false -> Color(0xFFF44336)
                    else -> AppTheme.colors.secondary
                },
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppText.labelLarge(
                    text = when {
                        success == true -> "🎯 Landing Successful"
                        attempt == true && success == false -> "❌ Landing Failed"
                        attempt == true -> "🔄 Landing Attempted"
                        else -> "No Landing Attempt"
                    },
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
private fun SpacecraftStagesSection(
    stages: List<SpacecraftStage>,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(text = stringResource(R.string.spacecraft_stages))
                AppText.labelMedium(
                    text = "${stages.size} ${if (stages.size == 1) stringResource(R.string.stage) else stringResource(R.string.stages)}",
                    color = AppTheme.colors.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            stages.forEachIndexed { index, stage ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = paddingXLarge),
                        color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
                    )
                }
                SpacecraftStageItem(stage = stage)
            }
        }
    }
}

@Composable
private fun SpacecraftStageItem(
    stage: SpacecraftStage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
    ) {
        stage.spacecraft?.let { spacecraft ->
            AppText.titleMedium(
                text = spacecraft.name ?: "Spacecraft",
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary
            )

            spacecraft.serialNumber?.let {
                AppText.bodyMedium(
                    text = "Serial: $it",
                    color = AppTheme.colors.secondary
                )
            }

            spacecraft.spacecraftConfig?.let { config ->
                Spacer(modifier = Modifier.height(paddingMedium))

                config.type?.name?.let { type ->
                    DetailRow(
                        label = "Type",
                        value = type,
                        icon = Icons.Default.Build
                    )
                }

                config.capability?.let { capability ->
                    Spacer(modifier = Modifier.height(paddingMedium))
                    AppText.bodyMedium(
                        text = capability,
                        color = AppTheme.colors.secondary
                    )
                }

                // Stats
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = paddingLarge),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    config.crewCapacity?.let {
                        MiniStatItem(
                            value = it.toString(),
                            label = "Crew Capacity",
                            icon = Icons.Default.Person
                        )
                    }

                    config.humanRated?.let { rated ->
                        MiniStatItem(
                            value = if (rated) "YES" else "NO",
                            label = "Human Rated",
                            icon = Icons.Default.CheckCircle,
                            iconTint = if (rated) Color(0xFF4CAF50) else AppTheme.colors.secondary
                        )
                    }
                }
            }
        }

        stage.destination?.let {
            Spacer(modifier = Modifier.height(paddingMedium))
            DetailRow(
                label = "Destination",
                value = it,
                icon = Icons.Default.Place
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    valueColor: Color = AppTheme.colors.onSurface
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = valueColor,
            modifier = Modifier.size(24.dp)
        )
        AppText.headlineSmall(
            text = value,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            textAlign = TextAlign.Center
        )
        AppText.bodySmall(
            text = label,
            color = AppTheme.colors.secondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MiniStatItem(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = AppTheme.colors.onSurface
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        AppText.titleSmall(
            text = value,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
        AppText.bodySmall(
            text = label,
            color = AppTheme.colors.secondary,
            textAlign = TextAlign.Center,
            fontSize = AppTextStyles.labelSmall.fontSize
        )
    }
}

@Composable
private fun InfoChip(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.colors.primary,
            modifier = Modifier.size(16.dp)
        )
        AppText.bodySmall(
            text = "$label: ",
            color = AppTheme.colors.secondary
        )
        AppText.bodySmall(
            text = value,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary
        )
    }
}

@Composable
private fun LinkButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.primary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(cornerRadiusMedium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingXLarge),
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.onSurface,
                modifier = Modifier.size(24.dp)
            )
            AppText.bodyLarge(
                text = text,
                color = AppTheme.colors.onSurface,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = AppTheme.colors.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun RocketSectionPreview() {
    AppTheme {
        RocketSection(
            rocket = previewData().rocket
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun RocketConfigurationCardPreview() {
    AppTheme {
        RocketConfigurationCard(
            config = previewData().rocket.configuration
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun LauncherStageItemPreview() {
    AppTheme {
        SectionCard {
            LauncherStageItem(
                stage = previewData().rocket.launcherStage!![0],
                index = 1
            )
        }
    }
}
