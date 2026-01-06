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
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.components.progress.shimmerEffect
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
    Box(modifier = modifier.fillMaxWidth()) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(
                        verticalGradient(
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
            // Mission name
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(32.dp)
                    .clip(RoundedCornerShape(cornerRadiusSmall))
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(paddingMedium))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status chip
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )

                // Launch date
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(22.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
private fun LoadingDetailsSection(modifier: Modifier = Modifier) {
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
                    .width(140.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(cornerRadiusSmall))
                    .shimmerEffect()
            )

            // Mission Highlight Card
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
                    // Header with icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // "MISSION" label
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(16.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
                            )
                            // Mission name
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
                            )
                        }
                        // Icon box in top right
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(cornerRadiusSmall))
                                .shimmerEffect()
                        )
                    }

                    // Mission Type & Orbit Chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mission type chip
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shimmerEffect()
                        )
                        // Orbit chip
                        Box(
                            modifier = Modifier
                                .width(110.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shimmerEffect()
                        )
                    }

                    // Description lines
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(if (it == 2) 0.6f else 1f)
                                    .height(16.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
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

            // Launch Window Timeline Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(cornerRadiusMedium))
                    .background(AppTheme.colors.primary.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header with icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // "LAUNCH WINDOW" label
                            Box(
                                modifier = Modifier
                                    .width(110.dp)
                                    .height(16.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
                            )
                            // Launch date
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
                            )
                        }
                        // Icon box in top right
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .shimmerEffect()
                        )
                    }

                    // Large launch time (centered)
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(180.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(cornerRadiusSmall))
                            .shimmerEffect()
                    )

                    // Timeline bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Timeline track with indicator
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .shimmerEffect()
                            )
                            // Window start and end times
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(18.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .shimmerEffect()
                                )
                                Box(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(18.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .shimmerEffect()
                                )
                            }
                        }
                    }

                    // Duration card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(cornerRadiusSmall))
                            .shimmerEffect()
                            .height(48.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingSiteSection(modifier: Modifier = Modifier) {
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
                    .shimmerEffect()
            )

            // Launch Site Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(cornerRadiusMedium))
                    .background(AppTheme.colors.primary.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header with icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // "LAUNCH SITE" label
                            Box(
                                modifier = Modifier
                                    .width(90.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
                            )
                            // Site name
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(22.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
                            )
                        }
                        // Icon box in top right
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(cornerRadiusSmall))
                                .shimmerEffect()
                        )
                    }
                }
            }

            // Location info (outside card)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .shimmerEffect()
                )
            }

            // Map image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(cornerRadiusMedium))
                    .shimmerEffect()
            )

            // Coordinates
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(cornerRadiusSmall))
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(cornerRadiusSmall))
                            .shimmerEffect()
                    )
                }
            }

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AppTheme.colors.onSurface.copy(alpha = 0.12f))
            )

            // Statistics title with icon
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(22.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .shimmerEffect()
                )
            }

            // Statistics chips (2x2 grid)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(cornerRadiusSmall))
                                .background(AppTheme.colors.primary.copy(alpha = 0.1f))
                                .padding(paddingLarge)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(24.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .shimmerEffect()
                                )
                                Box(
                                    modifier = Modifier
                                        .width(70.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .shimmerEffect()
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(cornerRadiusSmall))
                                .background(AppTheme.colors.primary.copy(alpha = 0.1f))
                                .padding(paddingLarge)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(24.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .shimmerEffect()
                                )
                                Box(
                                    modifier = Modifier
                                        .width(70.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .shimmerEffect()
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
private fun LoadingRocketSection(modifier: Modifier = Modifier) {
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
                    .shimmerEffect()
            )

            // Rocket Config Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(cornerRadiusMedium))
                    .background(AppTheme.colors.primary.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header with icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // "ROCKET" label
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
                            )
                            // Rocket name
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
                            )
                            // Variant
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(20.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
                            )
                            // Alias
                            Box(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(16.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
                            )
                        }
                        // Icon box in top right
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(cornerRadiusSmall))
                                .shimmerEffect()
                        )
                    }

                    // Rocket image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(cornerRadiusMedium))
                            .shimmerEffect()
                    )

                    // Description lines
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(if (it == 2) 0.7f else 1f)
                                    .height(16.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
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

            // "Launch Statistics" title
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(cornerRadiusSmall))
                    .shimmerEffect()
            )

            // Statistics chips (3 in a row)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(cornerRadiusSmall))
                            .background(AppTheme.colors.surfaceVariant.copy(alpha = 0.3f))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .shimmerEffect()
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(20.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .shimmerEffect()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .shimmerEffect()
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

            // "Physical Specifications" title
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(cornerRadiusSmall))
                    .shimmerEffect()
            )

            // Physical specs row (2 items)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(cornerRadiusSmall))
                            .background(AppTheme.colors.surfaceVariant.copy(alpha = 0.3f))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .shimmerEffect()
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .shimmerEffect()
                                )
                                Box(
                                    modifier = Modifier
                                        .width(70.dp)
                                        .height(20.dp)
                                        .clip(RoundedCornerShape(cornerRadiusSmall))
                                        .shimmerEffect()
                                )
                            }
                        }
                    }
                }
            }

            // Physical spec single item (launch mass)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(cornerRadiusSmall))
                    .background(AppTheme.colors.surfaceVariant.copy(alpha = 0.3f))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .shimmerEffect()
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(14.dp)
                                .clip(RoundedCornerShape(cornerRadiusSmall))
                                .shimmerEffect()
                        )
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(cornerRadiusSmall))
                                .shimmerEffect()
                        )
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

            // "Manufacturer and History" title
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(cornerRadiusSmall))
                    .shimmerEffect()
            )

            // Manufacturer details (4 rows)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                                .shimmerEffect()
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
                                    .shimmerEffect()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(20.dp)
                                    .clip(RoundedCornerShape(cornerRadiusSmall))
                                    .shimmerEffect()
                            )
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
