package com.seancoyle.launch.implementation.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        item {
            LazyRow {
                items(itemCount) { LoadingLaunchCarouselCard() }
            }
        }
        item { LoadingLaunchHeading() }
        items(itemCount) {
           Row(
               modifier = modifier.fillMaxWidth()
           ){
               LoadingLaunchGridCard(modifier = modifier.weight(1f))
               LoadingLaunchGridCard(modifier = modifier.weight(1f))
           }
        }
    }
}

@Composable
fun LoadingCompanySummaryCard(modifier: Modifier = Modifier) {
    ShimmerAnimation(getShimmerColors()) { brush ->
        Card(
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

@Composable
fun LoadingLaunchCarouselCard(modifier: Modifier = Modifier) {
    ShimmerAnimation(getShimmerColors()) { brush ->
        Card(
            modifier = modifier
                .size(120.dp)
                .padding(dimensionResource(id = R.dimen.small_view_margins_8dp)),
            backgroundColor = MaterialTheme.colors.surface,
            shape = CircleShape,
            elevation = 4.dp
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(brush = brush),
                contentAlignment = Alignment.Center
            ) {}
        }
    }
}

@Composable
fun LoadingLaunchGridCard(modifier: Modifier = Modifier) {
    ShimmerAnimation(getShimmerColors()) { brush ->
        Card(
            modifier = modifier
                .padding(dimensionResource(id = R.dimen.small_view_margins_8dp)),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.default_corner_radius))
        ) {
            Box(
                modifier = modifier
                    .size(170.dp)
                    .background(brush = brush)
            )
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