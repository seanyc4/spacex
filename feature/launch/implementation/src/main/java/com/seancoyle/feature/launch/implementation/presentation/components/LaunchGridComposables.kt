package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.feature.launch.api.LaunchTestTags.LAUNCH_CARD
import com.seancoyle.feature.launch.api.LaunchTestTags.LAUNCH_STATUS_ICON
import com.seancoyle.feature.launch.implementation.R
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUi
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents

@Composable
internal fun LaunchCard(
    launchItem: LaunchUi,
    onEvent: (LaunchesEvents) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {},
        shape = RoundedCornerShape(Dimens.dp10),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .semantics { testTag = LAUNCH_CARD }
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth(0.20f)
            ) {
                LaunchCardImage(imageUrl = launchItem.image)
            }

            Column(
                modifier = modifier
                    .fillMaxWidth(0.9f)
                    .padding(dimensionResource(R.dimen.small_view_margins_8dp))
            ) {
                LaunchCardDynamicText(
                    title = launchItem.missionName,
                    size = MaterialTheme.typography.titleSmall,
                    modifier = modifier.padding(bottom = Dimens.dp4)
                )
                LaunchCardDynamicText(
                    title = launchItem.launchDate,
                    size = MaterialTheme.typography.bodyMedium,
                    modifier = modifier.padding(bottom = Dimens.dp4)
                )
                LaunchCardDynamicText(
                    title = launchItem.launchDays,
                    size = MaterialTheme.typography.bodyMedium,
                    modifier = modifier.padding(bottom = Dimens.dp4)
                )
            }

            Column(
                modifier = modifier
                    .fillMaxWidth(1f)
            ) {
                Image(
                    painter = painterResource(id = launchItem.launchStatusIconResId),
                    contentDescription = LAUNCH_STATUS_ICON
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun LaunchCardImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    GlideImage(
        model = imageUrl,
        contentDescription = stringResource(id = R.string.launch_image),
        failure = placeholder(R.drawable.default_launch_image),
        modifier = modifier
            .fillMaxSize()
    )
}

@Composable
internal fun LaunchCardDefaultText(
    @StringRes title: Int
) {
    Text(
        text = stringResource(id = title),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
internal fun LaunchCardDynamicText(
    title: String,
    size: TextStyle = TextStyle.Default,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = size,
        color = MaterialTheme.colorScheme.primary,
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "Launch Card - Dark Mode")
@Composable
private fun LaunchCardPreview() {
    AppTheme(isDarkTheme = true) {
        LaunchCard(
            launchItem = LaunchUi(
                id = "1",
                missionName = "Starlink Mission",
                launchDate = "2024-01-15",
                launchStatus = LaunchStatus.SUCCESS,
                launchDays = "5 days from now",
                launchDaysResId = R.string.days_from_now,
                launchStatusIconResId = R.drawable.ic_launch_success,
                image = ""
            ),
            onEvent = {}
        )
    }
}
