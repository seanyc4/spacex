package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.core.ui.util.toCountryFlag

@Composable
internal fun LaunchDetailsSection(
    launch: LaunchUI,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Launch Details")

            launch.windowStart?.let {
                DetailRow(
                    label = "Window Start",
                    value = it,
                    icon = Icons.Default.DateRange
                )
            }

            launch.windowEnd?.let {
                DetailRow(
                    label = "Window End",
                    value = it,
                    icon = Icons.Default.DateRange
                )
            }

            launch.pad?.let { pad ->
                pad.location?.let { location ->
                    val siteName = pad.name ?: "Unknown"
                    val countryName = location.country?.name ?: ""
                    val countryFlag = location.country?.alpha2Code?.toCountryFlag() ?: ""

                    val siteValue = buildString {
                        append(siteName)
                        if (countryName.isNotEmpty()) {
                            append(" • ")
                            if (countryFlag.isNotEmpty()) {
                                append(countryFlag)
                                append(" ")
                            }
                            append(countryName)
                        }
                    }

                    DetailRow(
                        label = "Launch Site",
                        value = siteValue,
                        icon = Icons.Default.Place
                    )
                }
            }

            if (!launch.failReason.isNullOrEmpty()) {
                DetailRow(
                    label = "Fail Reason",
                    value = launch.failReason,
                    icon = Icons.Default.Warning,
                    valueColor = AppTheme.colors.error
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

