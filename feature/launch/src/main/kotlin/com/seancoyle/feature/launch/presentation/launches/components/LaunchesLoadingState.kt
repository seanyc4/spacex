package com.seancoyle.feature.launch.presentation.launches.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.components.progress.shimmerEffect
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.cornerRadiusXSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.launchCardHeight
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R

@Composable
fun LaunchesLoadingState(
    modifier: Modifier = Modifier
) {
    val loadingDescription = stringResource(R.string.loading_launch_details)
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = paddingMedium)
            .semantics {
                contentDescription = loadingDescription
                testTag = LaunchesTestTags.LOADING_STATE
            },
        contentPadding = PaddingValues(
            horizontal = paddingLarge,
            vertical = paddingMedium
        ),
        verticalArrangement = Arrangement.spacedBy(paddingMedium)
    ) {
        items(10) {
            LoadingLaunchCard()
        }
    }
}

@Composable
private fun LoadingLaunchCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(launchCardHeight)
            .clip(RoundedCornerShape(cornerRadiusLarge))
            .background(AppTheme.colors.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingLarge),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(cornerRadiusSmall))
                        .shimmerEffect()
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(24.dp)
                        .clip(RoundedCornerShape(cornerRadiusXSmall))
                        .shimmerEffect()
                )

                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(cornerRadiusXSmall))
                        .shimmerEffect()
                )
            }
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun LaunchesLoadingStatePreview() {
    AppTheme {
        LaunchesLoadingState()
    }
}

@PreviewDarkLightMode
@Composable
private fun LoadingLaunchCardPreview() {
    AppTheme {
        LoadingLaunchCard()
    }
}
