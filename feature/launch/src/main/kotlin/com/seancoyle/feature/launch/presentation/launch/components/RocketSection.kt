package com.seancoyle.feature.launch.presentation.launch.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTextStyles
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.domain.model.Configuration
import com.seancoyle.feature.launch.domain.model.Launcher
import com.seancoyle.feature.launch.domain.model.LauncherStage
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.R

@Composable
internal fun RocketSection(
    rocket: Rocket,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.dp8),
        verticalArrangement = Arrangement.spacedBy(Dimens.dp16)
    ) {
        // Header with image
        rocket.configuration?.let { config ->
            RocketHeader(config = config)
        }

        // Configuration Details Card
        rocket.configuration?.let { config ->
            ConfigurationCard(config = config)
        }

        // Launcher Stages
        if (!rocket.launcherStage.isNullOrEmpty()) {
            LauncherStagesSection(stages = rocket.launcherStage)
        }

        // Spacecraft Stages
        if (!rocket.spacecraftStage.isNullOrEmpty()) {
            SpacecraftStagesSection(stages = rocket.spacecraftStage)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RocketHeader(
    config: Configuration,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.dp16),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(Dimens.dp16)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            // Rocket Image
            GlideImage(
                model = config.image?.imageUrl ?: "",
                contentDescription = "Rocket ${config.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                failure = placeholder(R.drawable.default_launch_hero_image)
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            // Title overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Dimens.dp24)
            ) {
                AppText.headlineMedium(
                    text = config.fullName ?: config.name ?: "Unknown Rocket",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                config.variant?.let {
                    Spacer(modifier = Modifier.height(Dimens.dp8))
                    AppText.bodyLarge(
                        text = it,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfigurationCard(
    config: Configuration,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Rocket Configuration")

            // Description
            config.description?.let { desc ->
                AppText.bodyMedium(
                    text = desc,
                    color = AppTheme.colors.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = Dimens.dp8)
                )
            }

            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                config.totalLaunchCount?.let {
                    StatItem(
                        value = it.toString(),
                        label = "Total Launches",
                        icon = Icons.Default.Star
                    )
                }

                config.successfulLaunches?.let {
                    StatItem(
                        value = it.toString(),
                        label = "Successful",
                        icon = Icons.Default.CheckCircle,
                        valueColor = Color(0xFF4CAF50)
                    )
                }

                config.failedLaunches?.let {
                    if (it > 0) {
                        StatItem(
                            value = it.toString(),
                            label = "Failed",
                            icon = Icons.Default.Warning,
                            valueColor = Color(0xFFF44336)
                        )
                    }
                }
            }

            // Rocket Details
            HorizontalDivider(
                modifier = Modifier.padding(vertical = Dimens.dp8),
                color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
            )

            config.name?.let {
                DetailRow(
                    label = "Rocket Name",
                    value = it,
                    icon = Icons.Default.Star
                )
            }

            config.alias?.let {
                DetailRow(
                    label = "Alias",
                    value = it,
                    icon = Icons.Default.Info
                )
            }

            config.manufacturer?.name?.let {
                DetailRow(
                    label = "Manufacturer",
                    value = it,
                    icon = Icons.Default.Build
                )
            }

            // Families
            if (!config.families.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(Dimens.dp8))
                AppText.labelLarge(
                    text = "Rocket Families",
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(Dimens.dp8))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.dp8)
                ) {
                    items(config.families.size) { index ->
                        val family = config.families[index]
                        family.name?.let { familyName ->
                            Chip(
                                text = familyName,
                                contentColor = AppTheme.colors.primary,
                                containerColor = AppTheme.colors.primary
                            )
                        }
                    }
                }
            }

            // Links
            val context = LocalContext.current
            config.wikiUrl?.let { wikiUrl ->
                Spacer(modifier = Modifier.height(Dimens.dp8))
                LinkButton(
                    text = "View on Wikipedia",
                    icon = Icons.Default.Info,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl))
                        context.startActivity(intent)
                    }
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
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(text = "Booster Stages")
                AppText.labelMedium(
                    text = "${stages.size} ${if (stages.size == 1) "Stage" else "Stages"}",
                    color = AppTheme.colors.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            stages.forEachIndexed { index, stage ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = Dimens.dp12),
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
        verticalArrangement = Arrangement.spacedBy(Dimens.dp12)
    ) {
        // Stage header with image
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp16),
            verticalAlignment = Alignment.Top
        ) {
            // Launcher image
            stage.launcher?.image?.thumbnailUrl?.let { imageUrl ->
                GlideImage(
                    model = imageUrl,
                    contentDescription = "Launcher ${stage.launcher.serialNumber}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(Dimens.dp12))
                        .background(AppTheme.colors.surfaceVariant),
                    failure = placeholder(R.drawable.default_launch_hero_image)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.dp4)
            ) {
                AppText.titleMedium(
                    text = "Stage $index - ${stage.type ?: "Core"}",
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.onSurface
                )

                stage.launcher?.serialNumber?.let { serial ->
                    AppText.bodyMedium(
                        text = "Serial: $serial",
                        color = AppTheme.colors.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Reused status chip
                stage.reused?.let { isReused ->
                    Spacer(modifier = Modifier.height(Dimens.dp4))
                    Chip(
                        text = if (isReused) "♻️ Reused" else "🆕 New",
                        contentColor = if (isReused) Color(0xFF4CAF50) else AppTheme.colors.primary,
                        containerColor = if (isReused) Color(0xFF4CAF50) else AppTheme.colors.primary
                    )
                }
            }
        }

        // Launcher stats
        stage.launcher?.let { launcher ->
            LauncherStats(launcher = launcher)
        }

        // Landing info
        stage.landing?.let { landing ->
            LandingInfo(
                attempt = landing.attempt,
                success = landing.success,
                description = landing.description,
                locationName = landing.location?.name,
                typeName = landing.type?.name
            )
        }

        // Flight info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            stage.launcherFlightNumber?.let {
                InfoChip(
                    label = "Flight #",
                    value = it.toString(),
                    icon = Icons.Default.Info
                )
            }

            stage.launcher?.flights?.let {
                InfoChip(
                    label = "Total Flights",
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
        shape = RoundedCornerShape(Dimens.dp12)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.dp12),
            verticalArrangement = Arrangement.spacedBy(Dimens.dp8)
        ) {
            AppText.labelMedium(
                text = "Launcher Performance",
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                launcher.successfulLandings?.let { successful ->
                    launcher.attemptedLandings?.let { attempted ->
                        MiniStatItem(
                            value = "$successful/$attempted",
                            label = "Landings",
                            icon = Icons.Default.CheckCircle,
                            iconTint = Color(0xFF4CAF50)
                        )
                    }
                }

                launcher.flightProven?.let { proven ->
                    MiniStatItem(
                        value = if (proven) "YES" else "NO",
                        label = "Flight Proven",
                        icon = Icons.Default.Star,
                        iconTint = if (proven) Color(0xFFFFC107) else AppTheme.colors.onSurfaceVariant
                    )
                }
            }

            launcher.status?.name?.let { status ->
                AppText.bodySmall(
                    text = "Status: $status",
                    color = AppTheme.colors.onSurfaceVariant,
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
        shape = RoundedCornerShape(Dimens.dp12)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.dp12),
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp12),
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
                    else -> AppTheme.colors.onSurfaceVariant
                },
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.dp4)
            ) {
                AppText.labelLarge(
                    text = when {
                        success == true -> "🎯 Landing Successful"
                        attempt == true && success == false -> "❌ Landing Failed"
                        attempt == true -> "🔄 Landing Attempted"
                        else -> "No Landing Attempt"
                    },
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.onSurface
                )

                locationName?.let {
                    AppText.bodySmall(
                        text = "Location: $it",
                        color = AppTheme.colors.onSurfaceVariant
                    )
                }

                typeName?.let {
                    AppText.bodySmall(
                        text = "Type: $it",
                        color = AppTheme.colors.onSurfaceVariant
                    )
                }

                description?.let {
                    AppText.bodySmall(
                        text = it,
                        color = AppTheme.colors.onSurfaceVariant,
                        modifier = Modifier.padding(top = Dimens.dp4)
                    )
                }
            }
        }
    }
}

@Composable
private fun SpacecraftStagesSection(
    stages: List<com.seancoyle.feature.launch.domain.model.SpacecraftStage>,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(text = "Spacecraft Stages")
                AppText.labelMedium(
                    text = "${stages.size} ${if (stages.size == 1) "Stage" else "Stages"}",
                    color = AppTheme.colors.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            stages.forEachIndexed { index, stage ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = Dimens.dp12),
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
    stage: com.seancoyle.feature.launch.domain.model.SpacecraftStage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.dp12)
    ) {
        stage.spacecraft?.let { spacecraft ->
            AppText.titleMedium(
                text = spacecraft.name ?: "Spacecraft",
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.onSurface
            )

            spacecraft.serialNumber?.let {
                AppText.bodyMedium(
                    text = "Serial: $it",
                    color = AppTheme.colors.onSurfaceVariant
                )
            }

            spacecraft.spacecraftConfig?.let { config ->
                Spacer(modifier = Modifier.height(Dimens.dp8))

                config.type?.name?.let { type ->
                    DetailRow(
                        label = "Type",
                        value = type,
                        icon = Icons.Default.Build
                    )
                }

                config.capability?.let { capability ->
                    Spacer(modifier = Modifier.height(Dimens.dp8))
                    AppText.bodyMedium(
                        text = capability,
                        color = AppTheme.colors.onSurfaceVariant
                    )
                }

                // Stats
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.dp12),
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
                            iconTint = if (rated) Color(0xFF4CAF50) else AppTheme.colors.onSurfaceVariant
                        )
                    }
                }
            }
        }

        stage.destination?.let {
            Spacer(modifier = Modifier.height(Dimens.dp8))
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
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    valueColor: Color = AppTheme.colors.onSurface
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.dp4)
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
            color = AppTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MiniStatItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = AppTheme.colors.primary
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.dp4)
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
            color = AppTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontSize = AppTextStyles.labelSmall.fontSize
        )
    }
}

@Composable
private fun InfoChip(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.dp4),
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
            color = AppTheme.colors.onSurfaceVariant
        )
        AppText.bodySmall(
            text = value,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.onSurface
        )
    }
}

@Composable
private fun LinkButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
        shape = RoundedCornerShape(Dimens.dp12)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.dp16),
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )
            AppText.bodyLarge(
                text = text,
                color = AppTheme.colors.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = AppTheme.colors.primary,
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
            rocket = previewData().rocket!!
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun RocketHeaderPreview() {
    AppTheme {
        RocketHeader(
            config = previewData().rocket!!.configuration!!
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun LauncherStageItemPreview() {
    AppTheme {
        SectionCard {
            LauncherStageItem(
                stage = previewData().rocket!!.launcherStage!![0],
                index = 1
            )
        }
    }
}

