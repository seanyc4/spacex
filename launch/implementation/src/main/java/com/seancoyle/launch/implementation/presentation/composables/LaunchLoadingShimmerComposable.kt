package com.seancoyle.launch.implementation.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.seancoye.core.R
import com.seancoyle.core.presentation.composables.ShimmerAnimation

@Composable
fun getShimmerColors() = listOf(
    Color.Black,
    Color.Gray
)

@Composable
fun LoadingLaunchCardList(
    itemCount: Int,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        item { LoadingLaunchHeading() }
        item { LoadingCompanySummaryCard() }
        item { LoadingLaunchHeading() }
        items(itemCount) {
            LoadingLaunchCard()
        }
    }
}

@Composable
fun LoadingLaunchCard(modifier: Modifier = Modifier) {
    ShimmerAnimation(getShimmerColors()) { brush ->
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.small_view_margins_8dp)),
            backgroundColor = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius))
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .background(brush = brush)
            )
        }
    }
}

@Composable
fun LoadingCompanySummaryCard(modifier: Modifier = Modifier) {
    ShimmerAnimation(getShimmerColors()) { brush ->
        Card(
            backgroundColor = MaterialTheme.colors.secondary,
            elevation = 4.dp,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius)),
            modifier = modifier.padding(8.dp)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(brush = brush)
            )
        }
    }
}

@Composable
fun LoadingLaunchHeading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen._8sdp))
            .height(24.dp)
    )
}