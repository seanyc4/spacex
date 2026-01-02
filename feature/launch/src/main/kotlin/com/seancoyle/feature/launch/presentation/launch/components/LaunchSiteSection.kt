package com.seancoyle.feature.launch.presentation.launch.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.core.ui.util.toCountryFlag
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.domain.model.Pad

@Composable
internal fun LaunchSiteSection(
    pad: Pad,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.dp16)
    ) {
        // Launch Site Information Card
        LaunchSiteCard(pad = pad)

        // Launch Statistics Card
        val hasStats = pad.totalLaunchCount != null ||
                      pad.orbitalLaunchAttemptCount != null ||
                      pad.location?.totalLaunchCount != null

        if (hasStats) {
            LaunchStatisticsCard(pad = pad)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LaunchSiteCard(
    pad: Pad,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = stringResource(R.string.launch_site))

            // Header with Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.dp12),
                verticalAlignment = Alignment.Top
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = AppTheme.colors.primary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(Dimens.dp12)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Pad & Location Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Dimens.dp4)
                ) {
                    pad.name?.let { name ->
                        AppText.titleMedium(
                            text = name,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.onSurface
                        )
                    }

                    pad.location?.name?.let { location ->
                        AppText.bodyMedium(
                            text = location,
                            color = AppTheme.colors.onSurfaceVariant
                        )
                    }

                    pad.location?.country?.let { country ->
                        val countryFlag = country.alpha2Code?.toCountryFlag() ?: ""
                        val countryText = if (countryFlag.isNotEmpty()) {
                            "$countryFlag ${country.name ?: ""}"
                        } else {
                            country.name ?: ""
                        }

                        if (countryText.isNotBlank()) {
                            AppText.bodySmall(
                                text = countryText,
                                color = AppTheme.colors.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Description
            pad.description?.let { desc ->
                AppText.bodySmall(
                    text = desc,
                    color = AppTheme.colors.onSurfaceVariant
                )
            }

            // Map Image (clickable to open Google Maps)
            val mapImageUrl = pad.mapImage ?: pad.location?.mapImage
            val mapUrl = pad.mapUrl

            if (mapImageUrl != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(Dimens.dp12))
                        .then(
                            if (mapUrl != null) {
                                Modifier.clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
                                    context.startActivity(intent)
                                }
                            } else {
                                Modifier
                            }
                        )
                ) {
                    GlideImage(
                        model = mapImageUrl,
                        contentDescription = "Map of ${pad.name}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        failure = placeholder(R.drawable.default_launch_hero_image)
                    )

                    // Map overlay indicator
                    if (mapUrl != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(Dimens.dp8)
                                .background(
                                    color = AppTheme.colors.primary,
                                    shape = RoundedCornerShape(Dimens.dp8)
                                )
                                .padding(horizontal = Dimens.dp12, vertical = Dimens.dp8)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Dimens.dp4),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                AppText.labelSmall(
                                    text = stringResource(R.string.open_in_maps),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Coordinates
            val latitude = pad.latitude
            val longitude = pad.longitude
            if (latitude != null && longitude != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.dp8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        AppText.labelMedium(
                            text = stringResource(R.string.coordinates),
                            color = AppTheme.colors.onSurfaceVariant
                        )
                        AppText.bodyMedium(
                            text = "${String.format("%.6f", latitude)}, ${String.format("%.6f", longitude)}",
                            color = AppTheme.colors.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LaunchStatisticsCard(
    pad: Pad,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.dp8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                    modifier = Modifier.size(24.dp)
                )
                SectionTitle(text = stringResource(R.string.launch_statistics))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.dp8)
            ) {
                pad.totalLaunchCount?.let { count ->
                    PadStatChip(
                        label = stringResource(R.string.pad_launches),
                        value = count.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                pad.orbitalLaunchAttemptCount?.let { count ->
                    PadStatChip(
                        label = stringResource(R.string.orbital_attempts),
                        value = count.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            pad.location?.let { location ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.dp8)
                ) {
                    location.totalLaunchCount?.let { count ->
                        PadStatChip(
                            label = stringResource(R.string.location_launches),
                            value = count.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    location.totalLandingCount?.let { count ->
                        PadStatChip(
                            label = stringResource(R.string.location_landings),
                            value = count.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PadStatChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.primary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(Dimens.dp8)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.dp12),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.dp4)
        ) {
            AppText.titleMedium(
                text = value,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary,
                textAlign = TextAlign.Center
            )
            AppText.bodySmall(
                text = label,
                color = AppTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun LaunchSiteSectionPreview() {
    AppTheme {
        LaunchSiteSection(
            pad = previewData().pad!!
        )
    }
}
