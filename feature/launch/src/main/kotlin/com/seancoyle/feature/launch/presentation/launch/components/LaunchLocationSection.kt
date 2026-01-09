package com.seancoyle.feature.launch.presentation.launch.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.components.image.RemoteImage
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusXSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.core.ui.util.toCountryFlag
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.model.PadUI

@Composable
internal fun LaunchSiteSection(
    pad: PadUI,
    modifier: Modifier = Modifier
) {
    AppCard.Primary(modifier = modifier) {
        SectionTitle(text = stringResource(R.string.location))
        LaunchSiteContent(pad = pad)

        HorizontalDivider(
            modifier = Modifier.padding(vertical = paddingMedium),
            color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
        )
        LaunchStatisticsContent(pad = pad)
    }
}

@Composable
private fun LaunchSiteContent(
    pad: PadUI,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AppCard.Tinted(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                AppText.labelMedium(
                    text = stringResource(R.string.launch_site).uppercase(),
                    color = AppTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
                AppText.titleMedium(
                    text = pad.name,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.onSurface,
                    modifier = Modifier.padding(top = paddingSmall, end = paddingMedium)
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
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = paddingMedium),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!pad.locationName.isNullOrEmpty()) {
                AppText.bodyMedium(
                    text = pad.locationName,
                    color = AppTheme.colors.secondary
                )
            }

            if (!pad.countryName.isNullOrEmpty() && !pad.countryCode.isNullOrEmpty()) {
                val countryFlag = pad.countryCode.toCountryFlag()
                val countryText = if (countryFlag.isNotEmpty()) {
                    "$countryFlag ${pad.countryName}"
                } else {
                    pad.countryName
                }

                AppText.bodySmall(
                    text = countryText,
                    color = AppTheme.colors.secondary
                )
            }

            AppText.bodySmall(
                text = pad.description,
                color = AppTheme.colors.secondary
            )
        }

        if (pad.mapImage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(cornerRadiusMedium))
                    .then(
                        if (pad.mapUrl != null) {
                            Modifier.clickable {
                                val intent = Intent(Intent.ACTION_VIEW, pad.mapUrl.toUri())
                                context.startActivity(intent)
                            }
                        } else {
                            Modifier
                        }
                    )
            ) {
                RemoteImage(
                    imageUrl = pad.imageUrl,
                    contentDescription = stringResource(R.string.map_of_desc, pad.name),
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { testTag = LaunchesTestTags.LAUNCH_SITE_MAP },
                )

                if (pad.mapUrl != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(paddingMedium)
                            .background(
                                color = AppTheme.colors.inversePrimary,
                                shape = RoundedCornerShape(cornerRadiusXSmall)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                    .padding(top = paddingSmall),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = AppTheme.colors.onSurface,
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
    pad: PadUI,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = paddingSmall),
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = AppTheme.colors.primary,
                modifier = Modifier
                    .size(20.dp)
            )
            AppText.titleMedium(
                text = stringResource(R.string.site_statistics),
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingSmall)
        ) {
            PadStatChip(
                label = stringResource(R.string.pad_launches),
                value = pad.totalLaunchCount,
                modifier = Modifier.weight(1f)
            )

            PadStatChip(
                label = stringResource(R.string.orbital_attempts),
                value = pad.orbitalLaunchAttemptCount,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingSmall)
        ) {
            PadStatChip(
                label = stringResource(R.string.location_launches),
                value = pad.locationTotalLaunchCount,
                modifier = Modifier.weight(1f)
            )

            PadStatChip(
                label = stringResource(R.string.location_landings),
                value = pad.locationTotalLandingCount,
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
        shape = RoundedCornerShape(cornerRadiusXSmall)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AppText.titleMedium(
                text = value,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.onSurface,
                textAlign = TextAlign.Center
            )
            AppText.bodySmall(
                text = label,
                color = AppTheme.colors.secondary,
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
            pad = previewData().pad
        )
    }
}
