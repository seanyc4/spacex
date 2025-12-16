package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
internal fun LazyVerticalGridPagination(
    listState: LazyGridState,
    buffer: Int,
    onEvent: (LaunchEvents) -> Unit
) {
    val loadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItems - buffer)
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore }
            .distinctUntilChanged()
            .collect {
                if (it) onEvent(LaunchEvents.LoadNextPageEvent)
            }
    }
}
