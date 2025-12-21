package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUi

@Composable
@OptIn(ExperimentalMaterialApi::class)
internal fun SwipeToRefreshComposable(
    feedState: LazyPagingItems<LaunchUi>,
    pullRefreshState: PullRefreshState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            PullRefreshIndicator(
                refreshing = feedState.loadState.refresh is LoadState.Loading,
                state = pullRefreshState
            )
        }
    }
}
