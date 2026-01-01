package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.domain.model.Pad

@Composable
internal fun PadSection(
    pad: Pad,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Launch Pad")

            pad.name?.let {
                DetailRow(
                    label = "Name",
                    value = it,
                    icon = Icons.Default.Place
                )
            }

            pad.location?.name?.let {
                DetailRow(
                    label = "Location",
                    value = it,
                    icon = Icons.Default.LocationOn
                )
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
private fun PadSectionPreview() {
    AppTheme {
        PadSection(
            pad = previewData().pad!!
        )
    }
}
