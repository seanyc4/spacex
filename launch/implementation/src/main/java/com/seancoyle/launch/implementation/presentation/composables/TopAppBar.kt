package com.seancoyle.launch.implementation.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seancoyle.launch.implementation.R

@Composable
fun HomeAppBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = MaterialTheme.colors.primary,
                    fontSize = dimensionResource(id = R.dimen.text_size_heading).value.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(
                        Font(R.font.orbitron)
                    ),
                ),
                modifier = Modifier.align(Alignment.Center)
            )

            IconButton(
                onClick = { onClick() },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = dimensionResource(id = R.dimen.small_view_margins_8dp))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter_list),
                    contentDescription = "Filter List Button",
                    tint = colorResource(id = R.color.colorAccent)
                )
            }
        }
    }
}