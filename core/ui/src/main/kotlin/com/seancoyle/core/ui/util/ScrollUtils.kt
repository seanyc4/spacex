package com.seancoyle.core.ui.util

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun ObserveScrollPosition(
    listState: LazyGridState,
    onUpdateScrollPosition: (Int) -> Unit,
) {
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.debounce(750L)
            .collectLatest { position ->
                onUpdateScrollPosition(position)
            }
    }
}
