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
                value = agency.name,
                icon = Icons.Default.AccountCircle
            )

            DetailRow(
                label = "Abbreviation",
                value = agency.abbrev,
                icon = Icons.Default.Star
            )

            DetailRow(
                label = "Type",
                value = agency.type,
                icon = Icons.Default.Build
            )

            AppText.bodyMedium(
                text = agency.description,
                color = AppTheme.colors.onSurfaceVariant,
                modifier = Modifier.padding(top = Dimens.dp8)
            )
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
