package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppColors
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingXLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.model.SpacecraftStageUI

@Composable
internal fun SpacecraftStagesSection(
    stages: List<SpacecraftStageUI>,
    modifier: Modifier = Modifier
) {
    AppCard.Primary(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(text = stringResource(R.string.spacecraft_stages))
            AppText.labelMedium(
                text = "${stages.size} ${
                    if (stages.size == 1) stringResource(R.string.stage) else stringResource(
                        R.string.stages
                    )
                }",
                color = AppTheme.colors.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        stages.forEachIndexed { index, stage ->
            if (index > 0) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = paddingXLarge),
                    color = AppTheme.colors.onSurface.copy(alpha = 0.12f)
                )
            }
            SpacecraftStageItem(stage = stage)
        }
    }
}

@Composable
private fun SpacecraftStageItem(
    stage: SpacecraftStageUI,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
    ) {
        AppText.titleMedium(
            text = stage.spacecraft.name,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.primary
        )

        AppText.bodyMedium(
            text = stringResource(R.string.serial, stage.spacecraft.serialNumber),
            color = AppTheme.colors.secondary
        )

        Spacer(modifier = Modifier.height(paddingMedium))

        DetailRow(
            label = stringResource(R.string.type),
            value = stage.spacecraft.spacecraftConfig.type,
            icon = Icons.Default.Build
        )

        Spacer(modifier = Modifier.height(paddingMedium))
        AppText.bodyMedium(
            text = stage.spacecraft.spacecraftConfig.capability,
            color = AppTheme.colors.secondary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingLarge),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MiniStatItem(
                value = stage.spacecraft.spacecraftConfig.crewCapacity,
                label = stringResource(R.string.crew_capacity),
                icon = Icons.Default.Person
            )

            val rated = stage.spacecraft.spacecraftConfig.humanRated
            MiniStatItem(
                value = if (rated) stringResource(R.string.yes) else stringResource(R.string.no),
                label = stringResource(R.string.human_rated),
                icon = Icons.Default.CheckCircle,
                iconTint = if (rated) AppColors.success else AppTheme.colors.secondary
            )
        }
    }

    Spacer(modifier = Modifier.height(paddingMedium))
    DetailRow(
        label = stringResource(R.string.destination),
        value = stage.destination,
        icon = Icons.Default.Place
    )
}

@PreviewDarkLightMode
@Composable
private fun SpacecraftStagesSectionPreview() {
    AppTheme {
        SpacecraftStagesSection(
            stages = previewData().rocket.spacecraftStages
        )
    }
}
