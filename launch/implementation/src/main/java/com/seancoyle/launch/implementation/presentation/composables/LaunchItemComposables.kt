package com.seancoyle.launch.implementation.presentation.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.seancoyle.launch.api.domain.model.CompanySummary
import com.seancoyle.launch.api.domain.model.LaunchModel
import com.seancoyle.launch.api.domain.model.RocketWithMission
import com.seancoyle.launch.api.domain.model.SectionTitle
import com.seancoyle.launch.implementation.R

@Composable
fun LaunchHeading(
    launchHeading: SectionTitle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = launchHeading.title,
        color = MaterialTheme.colors.primary,
        style = TextStyle(
            fontFamily = FontFamily(
                Font(R.font.orbitron)
            ),
            fontSize = dimensionResource(id = R.dimen.text_size_subHeading).value.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen._8sdp))
    )
}

@Composable
fun CompanySummaryCard(
    summary: CompanySummary,
    modifier: Modifier = Modifier,
) {
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius)),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primaryVariant),
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = summary.summary,
            style = TextStyle(
                fontFamily = FontFamily(
                    Font(R.font.space_grotesk)
                ),
                fontSize = dimensionResource(id = R.dimen.text_size_medium).value.sp,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Start
            ),
            modifier = modifier
                .padding(dimensionResource(R.dimen.default_view_margin))
        )
    }
}

@Composable
fun LaunchCard(
    launchItem: LaunchModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small_view_margins_8dp))
            .clickable { onClick() },
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius))
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.small_view_margins_8dp))
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
                LaunchCardDefaultText(title = launchItem.daysToFromTitle)
            }

            Column(
                modifier = modifier
                    .fillMaxWidth(0.9f)
            ) {
                LaunchCardDynamicText(title = launchItem.missionName)
                LaunchCardDynamicText(title = launchItem.launchDate)
                LaunchCardDynamicText(title = launchItem.rocket.rocketNameAndType)
                LaunchCardDynamicText(title = launchItem.launchDaysDifference)
            }

            Column(
                modifier = modifier
                    .fillMaxWidth(1f)
            ) {
                Image(
                    painter = painterResource(id = launchItem.launchSuccessIcon),
                    contentDescription = "Success or failure icon"
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LaunchCardImage(
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
fun LaunchCardDefaultText(
    @StringRes title: Int
) {
    Text(
        text = stringResource(id = title),
        style = TextStyle(
            fontFamily = FontFamily(
                Font(R.font.space_grotesk)
            ),
            fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
            color = MaterialTheme.colors.primary
        )
    )
}

@Composable
fun LaunchCardDynamicText(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = TextStyle(
            fontFamily = FontFamily(
                Font(R.font.space_grotesk)
            ),
            fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
            color = MaterialTheme.colors.primaryVariant
        ),
        modifier = modifier
    )
}

@Composable
fun LaunchCarouselCard(
    launchItem: RocketWithMission,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small_view_margins_8dp))
            .clickable { onClick() },
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius))
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.default_view_margin)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchCardImage(
                imageUrl = launchItem.links.missionImage,
                size = 150.dp
            )
            LaunchCardDynamicText(
                title = launchItem.rocket.rocketNameAndType,
                modifier = modifier.padding(top = dimensionResource(id = R.dimen._6sdp))
            )
        }
    }
}

@Composable
fun LaunchGridCard(
    launchItem: LaunchModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small_view_margins_8dp))
            .clickable { onClick() },
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius))
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.default_view_margin)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchCardImage(
                imageUrl = launchItem.links.missionImage,
                size = 150.dp
            )
            LaunchCardDynamicText(
                title = launchItem.rocket.rocketNameAndType,
                modifier = modifier.padding(dimensionResource(id = R.dimen.default_view_margin))
            )
        }
    }
}