package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.domain.model.Agency
import com.seancoyle.feature.launch.domain.model.Mission
import com.seancoyle.feature.launch.domain.model.Pad
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.presentation.launch.previewData

@Composable
internal fun AgencySection(
    agency: Agency,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Launch Provider")

            DetailRow(
                label = "Name",
                value = agency.name ?: "Unknown",
                icon = Icons.Default.AccountCircle
            )
            agency.abbrev?.let {
                DetailRow(label = "Abbreviation", value = it, icon = Icons.Default.Star)
            }
            agency.type?.let {
                DetailRow(label = "Type", value = it, icon = Icons.Default.Build)
            }
            agency.description?.let {
                AppText.bodyMedium(
                    text = it,
                    color = AppTheme.colors.onSurfaceVariant,
                    modifier = Modifier.padding(top = Dimens.dp8)
                )
            }
        }
    }
}

@Composable
internal fun RocketSection(
    rocket: Rocket,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Rocket Configuration")

            rocket.configuration?.let { config ->
                config.name?.let {
                    DetailRow(label = "Name", value = it, icon = Icons.Default.Star)
                }
                config.fullName?.let {
                    DetailRow(label = "Full Name", value = it, icon = Icons.Default.Info)
                }
                config.variant?.let {
                    DetailRow(label = "Variant", value = it, icon = Icons.Default.Star)
                }
            }
        }
    }
}

@Composable
internal fun MissionSection(
    mission: Mission,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Mission Information")

            mission.name?.let {
                DetailRow(label = "Name", value = it, icon = Icons.Default.Star)
            }
            mission.type?.let {
                DetailRow(label = "Type", value = it, icon = Icons.Default.Build)
            }
            mission.orbit?.name?.let {
                DetailRow(label = "Orbit", value = it, icon = Icons.Default.Star)
            }
            mission.description?.let {
                AppText.bodyMedium(
                    text = it,
                    color = AppTheme.colors.onSurfaceVariant,
                    modifier = Modifier.padding(top = Dimens.dp8)
                )
            }
        }
    }
}

@Composable
internal fun PadSection(
    pad: Pad,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Launch Pad")

            pad.name?.let {
                DetailRow(label = "Name", value = it, icon = Icons.Default.Place)
            }
            pad.location?.name?.let {
                DetailRow(label = "Location", value = it, icon = Icons.Default.LocationOn)
            }
            pad.latitude?.let { lat ->
                pad.longitude?.let { lon ->
                    DetailRow(
                        label = "Coordinates",
                        value = "$lat, $lon",
                        icon = Icons.Default.Place
                    )
                }
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun AgencySectionPreview() {
    AppTheme {
        AgencySection(
            agency = previewData().launchServiceProvider!!
        )
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
private fun MissionSectionPreview() {
    AppTheme {
        MissionSection(
            mission = previewData().mission!!
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun PadSectionPreview() {
    AppTheme {
        PadSection(
            pad = previewData().pad!!
        )
    }
}
