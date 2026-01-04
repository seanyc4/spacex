package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.feature.launch.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun LaunchHeroSection(
    launch: LaunchUI,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box {
            GlideImage(
                model = launch.image.imageUrl,
                contentDescription = "Mission image for ${launch.missionName}",
                failure = placeholder(R.drawable.default_launch_hero_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                contentScale = ContentScale.Crop
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
                .padding(Dimens.dp24)
                .fillMaxWidth()
        ) {
            AppText.headlineLarge(
                text = launch.missionName,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary,
                modifier = Modifier.semantics {
                    contentDescription = "Mission name: ${launch.missionName}"
                }
            )

            Spacer(modifier = Modifier.height(Dimens.dp8))

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.dp8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Chip(
                    text = launch.status.label,
                    containerColor = launch.status.containerColor,
                    contentColor = launch.status.contentColor,
                    icon = launch.status.icon
                )

                AppText.bodyLarge(
                    text = launch.launchDate,
                    color = AppTheme.colors.secondary,
                    modifier = Modifier.semantics {
                        contentDescription = "Launch date: ${launch.launchDate}"
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
