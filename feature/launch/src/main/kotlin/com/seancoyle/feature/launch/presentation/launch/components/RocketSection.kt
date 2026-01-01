package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.presentation.launch.previewData

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
                    DetailRow(
                        label = "Name",
                        value = it,
                        icon = Icons.Default.Star
                    )
                }

                config.fullName?.let {
                    DetailRow(
                        label = "Full Name",
                        value = it,
                        icon = Icons.Default.Info
                    )
                }

                config.variant?.let {
                    DetailRow(
                        label = "Variant",
                        value = it,
                        icon = Icons.Default.Star
                    )
                }
            }
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
