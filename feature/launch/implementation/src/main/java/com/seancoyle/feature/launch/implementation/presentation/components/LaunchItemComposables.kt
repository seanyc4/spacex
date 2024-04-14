package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.RocketWithMission
import com.seancoyle.feature.launch.implementation.R

@Composable
internal fun LaunchHeading(
    launchHeading: LaunchTypes.SectionTitle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = launchHeading.title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen._8sdp))
            .semantics { testTag = "SECTION HEADING" }
    )
}

@Composable
internal fun CompanySummaryCard(
    companySummary: String,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius)),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = companySummary,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Start,
            modifier = modifier
                .padding(dimensionResource(R.dimen.default_view_margin))
                .semantics { testTag = "HEADER" }
        )
    }
}

@Composable
internal fun LaunchCard(
    launchItem: LaunchTypes.Launch,
    onClick: () -> Unit,
    getLaunchStatusIcon: Int,
    getLaunchDate: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small_view_margins_8dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius)),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.small_view_margins_8dp))
                .semantics { testTag = "LAUNCH CARD" }
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth(0.20f)
                    .padding(end = dimensionResource(id = R.dimen.small_view_margins_8dp))
                    .align(Alignment.CenterVertically)
            ) {
                LaunchCardImage(
                    imageUrl = launchItem.links.missionImage,
                    size = 60.dp
                )
            }

            Column(
                modifier = modifier
                    .fillMaxWidth(0.36f)
            ) {
                LaunchCardDefaultText(title = R.string.mission)
                LaunchCardDefaultText(title = R.string.date_time)
                LaunchCardDefaultText(title = R.string.rocket)
                LaunchCardDefaultText(title = getLaunchDate)
            }

            Column(
                modifier = modifier
                    .fillMaxWidth(0.9f)
            ) {
                LaunchCardDynamicText(title = launchItem.missionName)
                LaunchCardDynamicText(title = launchItem.launchDate)
                LaunchCardDynamicText(title = launchItem.rocket.rocketNameAndType)
                LaunchCardDynamicText(title = launchItem.launchDays)
            }

            Column(
                modifier = modifier
                    .fillMaxWidth(1f)
            ) {
                Image(
                    painter = painterResource(id = getLaunchStatusIcon),
                    contentDescription = "Launch Status icon"
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun LaunchCardImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    size: Dp
) {
    GlideImage(
        model = imageUrl,
        contentDescription = stringResource(id = R.string.launch_image),
        modifier = modifier
            .size(size)
            .padding(dimensionResource(id = R.dimen._4sdp))

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
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
internal fun LaunchCarouselCard(
    launchItem: RocketWithMission,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .size(120.dp)
            .padding(dimensionResource(id = R.dimen.small_view_margins_8dp))
            .clickable { onClick() },
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.small_view_margins_8dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LaunchCardImage(
                imageUrl = launchItem.links.missionImage,
                size = 100.dp
            )
        }
    }
}

@Composable
internal fun LaunchGridCard(
    launchItem: LaunchTypes.Grid,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.small_view_margins_8dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius)),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
    ) {
        Column(
            modifier = modifier
                .padding(dimensionResource(id = R.dimen.default_view_margin))
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchCardImage(
                imageUrl = launchItem.links.missionImage,
                size = 100.dp
            )
            LaunchCardDynamicText(
                title = launchItem.rocket.rocketNameAndType,
                modifier = modifier
                    .padding(top = dimensionResource(id = R.dimen.small_view_margins_8dp))
            )
        }
    }
}