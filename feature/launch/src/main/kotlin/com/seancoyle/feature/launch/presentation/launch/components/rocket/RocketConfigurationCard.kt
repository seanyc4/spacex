package com.seancoyle.feature.launch.presentation.launch.components.rocket

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.seancoyle.core.ui.components.image.RemoteImage
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppColors
import com.seancoyle.core.ui.designsystem.theme.AppTextStyles
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusXSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.domain.model.Configuration
import com.seancoyle.feature.launch.presentation.launch.components.DetailRow
import com.seancoyle.feature.launch.presentation.launch.components.SectionTitle
import com.seancoyle.feature.launch.presentation.launch.components.previewData

@Composable
internal fun RocketConfigurationCard(
    config: Configuration,
    modifier: Modifier = Modifier
) {
    AppCard.Primary(modifier = modifier) {
        SectionTitle(text = stringResource(R.string.rocket_config))
        RocketHeader(config = config)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = paddingLarge),
            color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
        )
        LaunchStatistics(config = config)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = paddingLarge),
            color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
        )
        PhysicalSpecifications(config = config)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = paddingLarge),
            color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
        )
        ManufacturerAndHistory(config = config)

        if (!config.families.isNullOrEmpty()) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = paddingLarge),
                color = AppTheme.colors.primary.copy(alpha = 0.1f)
            )
            AppText.titleMedium(
                text = stringResource(R.string.rocket_family),
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary,
                modifier = Modifier.padding(bottom = paddingSmall)
            )
            config.families.forEach { family ->
                RocketFamilyCard(family = family)
            }
        }

        ExternalLinks(config = config)
    }
}

@Composable
private fun RocketHeader(
    config: Configuration,
    modifier: Modifier = Modifier
) {
    AppCard.Tinted(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                AppText.labelMedium(
                    text = stringResource(R.string.rocket).uppercase(),
                    color = AppTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
                AppText.titleMedium(
                    text = config.fullName ?: config.name
                    ?: stringResource(R.string.unknown_rocket),
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
            RemoteImage(
                imageUrl = imageUrl,
                contentDescription = stringResource(
                    R.string.rocket_desc,
                    config.name.orEmpty()
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(cornerRadiusMedium)),
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

@Composable
private fun LaunchStatistics(
    config: Configuration,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
    ) {
        AppText.titleMedium(
            text = stringResource(R.string.launch_statistics),
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingMedium),
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
                        valueColor = AppColors.error,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            config.successfulLaunches?.let {
                StatItem(
                    value = it.toString(),
                    label = stringResource(R.string.successful),
                    icon = Icons.Default.CheckCircle,
                    valueColor = AppColors.success,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PhysicalSpecifications(
    config: Configuration,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
    ) {
        AppText.titleMedium(
            text = stringResource(R.string.physical_specifications),
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary
        )

        Column(
            modifier = Modifier.padding(top = paddingMedium),
            verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
        ) {
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
                .fillMaxWidth(),
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
private fun ManufacturerAndHistory(
    config: Configuration,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
    ) {
        AppText.titleMedium(
            text = stringResource(R.string.manufacturer_and_history),
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary
        )

        Column(
            modifier = Modifier.padding(top = paddingMedium),
            verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
        ) {
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
    }
}

@Composable
private fun ExternalLinks(
    config: Configuration,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hasLinks = config.wikiUrl != null || config.manufacturer?.wikiUrl != null

    if (hasLinks) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
        ) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = paddingLarge),
                color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
            )

            AppText.titleMedium(
                text = stringResource(R.string.learn_more),
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary
            )

            Column(
                modifier = Modifier.padding(top = paddingMedium),
                verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingSmall)
            ) {
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

@PreviewDarkLightMode
@Composable
private fun RocketConfigurationCardPreview() {
    AppTheme {
        RocketConfigurationCard(
            config = previewData().rocket.configuration
        )
    }
}
