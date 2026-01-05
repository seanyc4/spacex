package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.components.progress.ShimmerAnimation
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingXLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.LaunchesTestTags

@Composable
internal fun LoadingState(modifier: Modifier = Modifier) {
    val loadingDescription = stringResource(R.string.loading_launch_details)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                contentDescription = loadingDescription
                testTag = LaunchesTestTags.LOADING_STATE
            },
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Section Skeleton
        item { LoadingHeroSection() }
        item { Spacer(modifier = Modifier.height(paddingXLarge)) }

        // Launch Details Section Skeleton
        item { LoadingDetailsSection() }

        item { Spacer(modifier = Modifier.height(paddingXLarge)) }

        // Launch Site Section Skeleton
        item { LoadingSiteSection() }

        item { Spacer(modifier = Modifier.height(paddingXLarge)) }

        // Rocket Section Skeleton
        item { LoadingRocketSection() }
    }
}

@Composable
private fun LoadingHeroSection(modifier: Modifier = Modifier) {
    ShimmerAnimation(
        colors = listOf(
            AppTheme.colors.surface.copy(alpha = 0.25f),
            AppTheme.colors.primary.copy(alpha = 0.15f),
            AppTheme.colors.surface.copy(alpha = 0.25f)
        )
    ) { brush ->
        Box(modifier = modifier.fillMaxWidth()) {
            // Background image placeholder with gradient overlay
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .background(brush)
                )

                // Gradient overlay to match real screen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    androidx.compose.ui.graphics.Color.Transparent,
                                    AppTheme.colors.background.copy(alpha = 0.7f)
                                ),
                                startY = 100f
                            )
                        )
                )
            }

            // Content overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                // Mission name skeleton (headlineLarge style)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(32.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(paddingMedium))

                // Status chip and date row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status chip placeholder
                    Box(
                        modifier = Modifier
                            .width(90.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(brush)
                    )
                    // Date text placeholder (bodyLarge style)
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(22.dp)
                            .clip(RoundedCornerShape(cornerRadiusSmall))
                            .background(brush)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingDetailsSection(modifier: Modifier = Modifier) {
    ShimmerAnimation(
        colors = listOf(
            AppTheme.colors.surface.copy(alpha = 0.25f),
            AppTheme.colors.primary.copy(alpha = 0.15f),
            AppTheme.colors.surface.copy(alpha = 0.25f)
        )
    ) { brush ->
        // Mimic SectionCard wrapper
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = paddingXLarge)
                .clip(RoundedCornerShape(cornerRadiusLarge))
                .background(AppTheme.colors.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Section Title (titleLarge style)
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .background(brush)
                )

                // Mission highlight card - inner card with primary background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cornerRadiusMedium))
                        .background(AppTheme.colors.primary.copy(alpha = 0.05f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Label
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(cornerRadiusSmall))
                                .background(brush)
                        )
                        // Title
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(24.dp)
                                .clip(RoundedCornerShape(cornerRadiusSmall))
                                .background(brush)
                        )
                        // Subtitle/type
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(cornerRadiusSmall))
                                .background(brush)
                        )
                        // Description lines
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(if (it == 2) 0.6f else 1f)
                                        .height(16.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                            }
                        }
                    }
                }

                // Divider placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(AppTheme.colors.onSurface.copy(alpha = 0.12f))
                )

                // Timeline section
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    repeat(3) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(brush)
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.7f)
                                        .height(18.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingSiteSection(modifier: Modifier = Modifier) {
    ShimmerAnimation(
        colors = listOf(
            AppTheme.colors.surface.copy(alpha = 0.25f),
            AppTheme.colors.primary.copy(alpha = 0.15f),
            AppTheme.colors.surface.copy(alpha = 0.25f)
        )
    ) { brush ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = paddingXLarge)
                .clip(RoundedCornerShape(cornerRadiusLarge))
                .background(AppTheme.colors.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Section Title
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .background(brush)
                )

                // Location card with inner styling
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cornerRadiusMedium))
                        .background(AppTheme.colors.primary.copy(alpha = 0.05f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Label
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                                // Site name
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .height(22.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                            }
                            // Icon placeholder
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .background(brush)
                            )
                        }

                        // Additional location details
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            repeat(2) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(brush)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .width(150.dp)
                                            .height(18.dp)
                                            .clip(RoundedCornerShape(cornerRadiusSmall))
                                            .background(brush)
                                    )
                                }
                            }
                        }
                    }
                }

                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(AppTheme.colors.onSurface.copy(alpha = 0.12f))
                )

                // Stats grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(2) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(cornerRadiusMedium))
                                .background(AppTheme.colors.primary.copy(alpha = 0.05f))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(brush)
                            )
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .background(brush)
                            )
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .background(brush)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingRocketSection(modifier: Modifier = Modifier) {
    ShimmerAnimation(
        colors = listOf(
            AppTheme.colors.surface.copy(alpha = 0.25f),
            AppTheme.colors.primary.copy(alpha = 0.15f),
            AppTheme.colors.surface.copy(alpha = 0.25f)
        )
    ) { brush ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = paddingXLarge)
                .clip(RoundedCornerShape(cornerRadiusLarge))
                .background(AppTheme.colors.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Section Title
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .background(brush)
                )

                // Rocket configuration card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cornerRadiusMedium))
                        .background(AppTheme.colors.primary.copy(alpha = 0.05f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Label
                                Box(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                                // Rocket name
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.7f)
                                        .height(22.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                                // Variant
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(20.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                                // Alias
                                Box(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(16.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                            }
                            // Icon placeholder
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .background(brush)
                            )
                        }

                        // Image placeholder
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(cornerRadiusMedium))
                                .background(brush)
                        )

                        // Description lines
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(if (it == 2) 0.7f else 1f)
                                        .height(16.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                            }
                        }
                    }
                }

                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(AppTheme.colors.onSurface.copy(alpha = 0.12f))
                )

                // Rocket details rows
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    repeat(4) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(brush)
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.6f)
                                        .height(20.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun LoadingStatePreview() {
    AppTheme {
        LoadingState()
    }
}
