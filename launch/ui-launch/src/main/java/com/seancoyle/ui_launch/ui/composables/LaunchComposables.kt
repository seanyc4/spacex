package com.seancoyle.ui_launch.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seancoyle.ui_launch.R

@Composable
fun LaunchBottomSheetDivider() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(id = R.color.colorAccent))
    )
}

@Composable
fun LaunchBottomSheetCard(
    articleLink: String?,
    webcastLink: String?,
    wikiLink: String?,
    onArticleLinkClick: () -> Unit,
    onWebcastLinkClick: () -> Unit,
    onWikiLinkClick: () -> Unit,
    onExitBtn: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.small_view_margins_8dp),
                end = dimensionResource(id = R.dimen.small_view_margins_8dp)
            ),
        backgroundColor = colorResource(id = R.color.colorSecondary),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LaunchBottomSheetHeader()
            LaunchBottomSheetDivider()
            if(!articleLink.isNullOrEmpty()) {
                LaunchBottomSheetTitle(
                    name = stringResource(id = R.string.article),
                    onArticleLinkClick
                )
                LaunchBottomSheetDivider()
            }
            if(!webcastLink.isNullOrEmpty()) {
                LaunchBottomSheetTitle(
                    name = stringResource(id = R.string.webcast),
                    onWebcastLinkClick
                )
                LaunchBottomSheetDivider()
            }
            if(!wikiLink.isNullOrEmpty()) {
                LaunchBottomSheetTitle(
                    name = stringResource(id = R.string.wikipedia),
                    onWikiLinkClick
                )
            }
        }
    }
}

@Composable
fun LaunchBottomSheetHeader() {
    Text(
        text = stringResource(id = R.string.links),
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontFamily = FontFamily(
                Font(R.font.orbitron)
            ),
            fontSize = dimensionResource(id = R.dimen.text_size_medium).value.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.textColorPrimary)
        ),
        modifier = Modifier
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .padding(dimensionResource(id = R.dimen.default_bottom_sheet_margin))
            .focusable(enabled = true)
    )
}

@Composable
fun LaunchBottomSheetTitle(
    name: String,
    onClick: () -> Unit
) {
    Text(
        text = name,
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontFamily = FontFamily(
                Font(R.font.space_grotesk)
            ),
            fontSize = dimensionResource(id = R.dimen.text_size_heading).value.sp,
            color = colorResource(id = R.color.colorAccent)
        ),
        modifier = Modifier
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .padding(dimensionResource(id = R.dimen.default_bottom_sheet_margin))
            .focusable(enabled = true)
            .clickable {
                onClick()
            }
    )
}

@Composable
fun LaunchBottomSheetExitButton(
    onExitBtn: () -> Unit
) {
    Button(
        onClick = onExitBtn,
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 80.dp)
            .padding(dimensionResource(id = R.dimen.default_bottom_sheet_margin)),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.colorSecondary)),
        shape = RoundedCornerShape(size = dimensionResource(id = R.dimen.default_corner_radius))
    ) {
        Text(
            text = stringResource(id = R.string.text_cancel),
            color = colorResource(id = R.color.colorError),
            fontSize = dimensionResource(id = R.dimen.text_size_heading).value.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun LaunchBottomSheetTitlesPreview() {
    MaterialTheme {
        LaunchBottomSheetHeader()
    }
}
