package com.seancoyle.launch.implementation.presentation.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.seancoyle.launch.api.model.CompanySummary
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.SectionTitle
import com.seancoyle.launch.implementation.R

@Composable
fun LaunchHeading(
    launchHeading: SectionTitle
) {
    Text(
        text = launchHeading.title,
        color = colorResource(id = R.color.textColorPrimary),
        style = TextStyle(
            fontFamily = FontFamily(
                Font(R.font.orbitron)
            ),
            fontSize = dimensionResource(id = R.dimen.text_size_subHeading).value.sp,
            color = colorResource(id = R.color.textColorPrimary),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen._8sdp))
    )
}

@Composable
fun CompanySummaryCard(
    summary: CompanySummary
) {
    Card(
        backgroundColor = colorResource(id = R.color.colorSecondary),
        elevation = 4.dp,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius)),
        border = BorderStroke(width = 1.dp, color = colorResource(id = R.color.colorAccent)),
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = summary.summary,
            style = TextStyle(
                fontFamily = FontFamily(
                    Font(R.font.space_grotesk)
                ),
                fontSize = dimensionResource(id = R.dimen.text_size_medium).value.sp,
                color = colorResource(id = R.color.textColorPrimary),
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.default_view_margin))
        )
    }
}

@Composable
fun LaunchCard(
    launchItem: LaunchModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small_view_margins_8dp))
            .clickable { onClick() },
        backgroundColor = colorResource(id = R.color.colorSecondary),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.small_view_margins_8dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.20f)
                    .padding(end = dimensionResource(id = R.dimen.small_view_margins_8dp))
                    .align(Alignment.CenterVertically)
            ) {
                LaunchCardImage(imageUrl = launchItem.links.missionImage)
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth(0.40f)
            ) {
                LaunchCardDefaultText(title = R.string.mission)
                LaunchCardDefaultText(title = R.string.date_time)
                LaunchCardDefaultText(title = R.string.rocket)
                LaunchCardDefaultText(title = launchItem.daysToFromTitle)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                LaunchCardDynamicText(title = launchItem.missionName)
                LaunchCardDynamicText(title = launchItem.launchDate)
                LaunchCardDynamicText(title = launchItem.rocket.rocketNameAndType)
                LaunchCardDynamicText(title = launchItem.launchDaysDifference)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LaunchCardImage(
    imageUrl: String
) {
    GlideImage(
        model = imageUrl,
        contentDescription = stringResource(id = R.string.launch_image),
        modifier = Modifier
            .size(60.dp)
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
            color = colorResource(id = R.color.textColorPrimary)
        )
    )
}

@Composable
fun LaunchCardDynamicText(
    title: String
) {
    Text(
        text = title,
        style = TextStyle(
            fontFamily = FontFamily(
                Font(R.font.space_grotesk)
            ),
            fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
            color = colorResource(id = R.color.colorAccent)
        )
    )
}