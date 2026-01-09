package com.seancoyle.feature.launch.presentation.launch.components.rocket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.presentation.launch.components.previewData
import com.seancoyle.feature.launch.presentation.launch.model.RocketUI

/**
 * Main rocket section that orchestrates the display of rocket-related information.
 * This composable delegates to smaller, focused components for each section.
 *
 * Components:
 * - RocketConfigurationCard: Displays rocket configuration, specs, and statistics
 * - LauncherStagesSection: Displays booster stage information
 * - SpacecraftStagesSection: Displays spacecraft stage information
 */
@Composable
internal fun RocketSection(
    rocket: RocketUI,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
    ) {
        RocketConfigurationCard(config = rocket.configuration)

        if (rocket.launcherStages.isNotEmpty()) {
            LauncherStagesSection(stages = rocket.launcherStages)
        }

        if (rocket.spacecraftStages.isNotEmpty()) {
            SpacecraftStagesSection(stages = rocket.spacecraftStages)
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun RocketSectionPreview() {
    AppTheme {
        RocketSection(
            rocket = previewData().rocket
        )
    }
}
