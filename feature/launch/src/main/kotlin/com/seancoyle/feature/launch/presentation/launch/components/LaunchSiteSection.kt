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
import androidx.compose.material3.HorizontalDivider
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
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = stringResource(R.string.launch_site))
            LaunchSiteContent(pad = pad)

            val hasStats = pad.totalLaunchCount != null ||
                    pad.orbitalLaunchAttemptCount != null ||
                    pad.location?.totalLaunchCount != null

            if (hasStats) {
                HorizontalDivider(color = AppTheme.colors.onSurface.copy(alpha = 0.12f))
                LaunchStatisticsContent(pad = pad)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LaunchSiteContent(
    pad: Pad,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                    AppText.titleMedium(
                        text = pad.name.orEmpty(),
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.onSurface,
                    )
                }
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = AppTheme.colors.primary.copy(alpha = 0.5f),
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(Dimens.dp4)
        ) {
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

            pad.description?.let { desc ->
                AppText.bodySmall(
                    text = desc,
                    color = AppTheme.colors.onSurfaceVariant
                )
            }
        }

        val mapImageUrl = pad.mapImage ?: pad.location?.mapImage
        val mapUrl = pad.mapUrl
        if (mapImageUrl != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
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

        val latitude = pad.latitude
        val longitude = pad.longitude
        if (latitude != null && longitude != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
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
                        text = "${String.format("%.6f", latitude)}, ${
                            String.format(
                                "%.6f",
                                longitude
                            )
                        }",
                        color = AppTheme.colors.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun LaunchStatisticsContent(
    pad: Pad,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.dp8)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = AppTheme.colors.primary,
                modifier = Modifier.size(20.dp)
            )
            AppText.titleMedium(
                text = stringResource(R.string.site_statistics),
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.onSurface
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp8)
        ) {
            PadStatChip(
                label = stringResource(R.string.pad_launches),
                value = pad.totalLaunchCount?.toString() ?: stringResource(R.string.not_available),
                modifier = Modifier.weight(1f)
            )

            PadStatChip(
                label = stringResource(R.string.orbital_attempts),
                value = pad.orbitalLaunchAttemptCount?.toString()
                    ?: stringResource(R.string.not_available),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp8)
        ) {
            PadStatChip(
                label = stringResource(R.string.location_launches),
                value = pad.location?.totalLaunchCount?.toString()
                    ?: stringResource(R.string.not_available),
                modifier = Modifier.weight(1f)
            )

            PadStatChip(
                label = stringResource(R.string.location_landings),
                value = pad.location?.totalLandingCount?.toString()
                    ?: stringResource(R.string.not_available),
                modifier = Modifier.weight(1f)
            )
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
