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
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
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
            AppTheme.colors.surface.copy(alpha = 0.3f),
            AppTheme.colors.primary.copy(alpha = 0.1f),
            AppTheme.colors.surface.copy(alpha = 0.3f)
        )
    ) { brush ->
        Box(modifier = modifier.fillMaxWidth()) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(brush)
            )

            // Content overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                // Mission name skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(paddingMedium))

                // Status chip and date row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(brush)
                    )
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(20.dp)
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
            AppTheme.colors.surface.copy(alpha = 0.3f),
            AppTheme.colors.primary.copy(alpha = 0.1f),
            AppTheme.colors.surface.copy(alpha = 0.3f)
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
                modifier = Modifier.padding(24.dp)
            ) {
                // Section Title
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(paddingLarge))

                // Mission highlight card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(cornerRadiusMedium))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(paddingLarge))

                // Timeline section
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(3) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(brush)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
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

@Composable
private fun LoadingSiteSection(modifier: Modifier = Modifier) {
    ShimmerAnimation(
        colors = listOf(
            AppTheme.colors.surface.copy(alpha = 0.3f),
            AppTheme.colors.primary.copy(alpha = 0.1f),
            AppTheme.colors.surface.copy(alpha = 0.3f)
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
                modifier = Modifier.padding(24.dp)
            ) {
                // Section Title
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(paddingLarge))

                // Location card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(cornerRadiusMedium))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(paddingLarge))

                // Stats grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(100.dp)
                                .clip(RoundedCornerShape(cornerRadiusMedium))
                                .background(brush)
                        )
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
            AppTheme.colors.surface.copy(alpha = 0.3f),
            AppTheme.colors.primary.copy(alpha = 0.1f),
            AppTheme.colors.surface.copy(alpha = 0.3f)
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
                modifier = Modifier.padding(24.dp)
            ) {
                // Section Title
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(paddingLarge))

                // Rocket image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(cornerRadiusMedium))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(paddingLarge))

                // Rocket details rows
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    repeat(4) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(brush)
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.4f)
                                        .height(16.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .background(brush)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.7f)
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
