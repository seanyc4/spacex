package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.components.image.RemoteImage
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.LaunchesTestTags
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.feature.launch.presentation.launch.model.containerColor
import com.seancoyle.feature.launch.presentation.launch.model.contentColor
import com.seancoyle.feature.launch.presentation.launch.model.icon

@Composable
internal fun LaunchHeroSection(
    launch: LaunchUI,
    modifier: Modifier = Modifier
) {
    val missionImageDesc = stringResource(R.string.mission_image, launch.missionName)
    val missionNameDesc = stringResource(R.string.mission_name_desc, launch.missionName)
    val launchDateDesc = stringResource(R.string.launch_date_desc, launch.launchDate)

    Box(modifier = modifier
        .fillMaxWidth()
        .semantics { testTag = LaunchesTestTags.LAUNCH_HERO_SECTION }
    ) {
        Box {
            RemoteImage(
                imageUrl = launch.image.imageUrl,
                contentDescription = missionImageDesc,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                AppTheme.colors.background.copy(alpha = 0.9f)
                            ),
                            startY = 100f
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            AppText.headlineLarge(
                text = launch.missionName,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary,
                modifier = Modifier.semantics {
                    contentDescription = missionNameDesc
                }
            )

            Spacer(modifier = Modifier.height(paddingMedium))

            Row(
                horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Chip(
                    text = launch.status.label,
                    containerColor = launch.status.containerColor(),
                    contentColor = launch.status.contentColor(),
                    icon = launch.status.icon()
                )

                AppText.bodyLarge(
                    text = launch.launchDate,
                    color = AppTheme.colors.secondary,
                    modifier = Modifier.semantics {
                        contentDescription = launchDateDesc
                    }
                )
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun LaunchHeroSectionPreview() {
    AppTheme {
        LaunchHeroSection(
            launch = previewData()
        )
    }
}
