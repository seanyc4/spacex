package com.seancoyle.feature.launch.presentation.launches.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.components.progress.ShimmerAnimation
import com.seancoyle.core.ui.designsystem.theme.Dimens

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
        item { LoadingLaunchHeading() }
        item {
            LazyRow {
                items(itemCount) { LoadingLaunchCarouselCard() }
            }
        }
        item { LoadingLaunchHeading() }
    }
}

@Composable
fun LoadingLaunchHeading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.dp8)
            .height(24.dp)
    )
}

@Composable
fun LoadingLaunchCarouselCard(modifier: Modifier = Modifier) {
    ShimmerAnimation(getShimmerColors()) { brush ->
        Card(
            modifier = modifier
                .size(120.dp)
                .padding(Dimens.dp8),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
fun LoadingLaunchCard(modifier: Modifier = Modifier) {
    ShimmerAnimation(getShimmerColors()) { brush ->
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(Dimens.dp8),
            shape = RoundedCornerShape(Dimens.dp8)
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
