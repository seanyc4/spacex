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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingMedium
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.domain.model.Agency

@Composable
internal fun LaunchProviderSection(
    agency: Agency,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)) {
            SectionTitle(text = "Launch Provider")

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.colors.primary.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(cornerRadiusMedium)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.dp20),
                    verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingMedium)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            AppText.labelMedium(
                                text = stringResource(R.string.name).uppercase(),
                                color = AppTheme.colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                            AppText.titleMedium(
                                text = agency.name,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.onSurface,
                                modifier = Modifier.padding(top = Dimens.dp2)
                            )
                            AppText.labelMedium(
                                text = stringResource(R.string.abbreviation).uppercase(),
                                color = AppTheme.colors.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = Dimens.dp14)
                            )
                            AppText.titleMedium(
                                text = agency.abbrev,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.onSurface,
                                modifier = Modifier.padding(top = Dimens.dp2)
                            )
                            AppText.labelMedium(
                                text = stringResource(R.string.type).uppercase(),
                                color = AppTheme.colors.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = Dimens.dp14)
                            )
                            AppText.titleMedium(
                                text = agency.type,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.onSurface,
                                modifier = Modifier.padding(top = Dimens.dp2)
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
                                contentDescription = null,
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
