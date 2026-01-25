package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.model.AgencyUI

@Composable
internal fun LaunchProviderSection(
    agency: AgencyUI,
    modifier: Modifier = Modifier
) {
    AppCard.Primary(
        modifier = modifier
            .semantics(mergeDescendants = true) {}
    ) {
        SectionTitle(text = stringResource(R.string.launch_provider))
        AppCard.Tinted(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AppText.labelMedium(
                        text = stringResource(R.string.name).uppercase(),
                        color = AppTheme.colors.primary,
                        fontWeight = FontWeight.Bold,

                        )
                    AppText.titleMedium(
                        text = agency.name,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.onSurface,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    AppText.labelMedium(
                        text = stringResource(R.string.abbreviation).uppercase(),
                        color = AppTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 14.dp),

                        )
                    AppText.titleMedium(
                        text = agency.abbrev,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.onSurface,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    AppText.labelMedium(
                        text = stringResource(R.string.type).uppercase(),
                        color = AppTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 14.dp),

                        )
                    AppText.titleMedium(
                        text = agency.type,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.onSurface,
                        modifier = Modifier.padding(top = 2.dp)
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
                        imageVector = Icons.Default.Business,
                        contentDescription = null, // decorative
                        tint = AppTheme.colors.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            AppText.bodyMedium(
                text = agency.description,
                color = AppTheme.colors.secondary,
                modifier = Modifier.padding(top = paddingMedium)
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun LaunchProviderSectionPreview() {
    AppTheme {
        LaunchProviderSection(
            agency = previewData().launchServiceProvider!!
        )
    }
}
