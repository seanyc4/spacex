package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.core.ui.util.openUrl
import com.seancoyle.feature.launch.presentation.launch.model.RocketUI

@Composable
internal fun RocketSection(
    rocket: RocketUI,
    modifier: Modifier = Modifier,
    onExternalLinkClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)
    ) {
        RocketConfigurationCard(
            config = rocket.configuration,
            onOpenUrl = { url ->
                onExternalLinkClick()
                context.openUrl(url)
            }
        )

        if (rocket.launcherStages.isNotEmpty()) {
            LauncherStagesSection(stages = rocket.launcherStages)
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
