package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchTypesUiModel
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.UpdateScrollPositionEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScreenState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

private const val GRID_COLUMN_SIZE = 2

@Composable
internal fun LaunchesGridContent(
    launches: LazyPagingItems<LaunchTypesUiModel>,
    screenState: LaunchesScreenState,
    onEvent: (LaunchEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyGridState(initialFirstVisibleItemIndex = screenState.scrollPosition)
    ObserveScrollPosition(listState, onEvent)

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_COLUMN_SIZE),
            state = listState,
            modifier = modifier.semantics { testTag = "Launch Grid" }
        ) {
            items(
                count = launches.itemCount,
                key = launches.itemKey { item ->
                    when (item) {
                        is LaunchTypesUiModel.LaunchUi -> item.id
                    }
                },
                span = { index ->
                    val item = launches[index]
                    GridItemSpan(
                        when (item) {
                            is LaunchTypesUiModel.LaunchUi -> GRID_COLUMN_SIZE
                            else -> 1
                        }
                    )
                }
            ) { index ->
                val launchItem = launches[index]
                if (launchItem != null) {
                    RenderGridSections(
                        launchItem = launchItem,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun ObserveScrollPosition(
    listState: LazyGridState,
    onEvent: (LaunchEvents) -> Unit,
) {
    // Observe and save scroll position to view model
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.debounce(750L)
            .collectLatest { position ->
                onEvent(UpdateScrollPositionEvent(position))
            }
    }
}

@Composable
private fun RenderGridSections(
    launchItem: LaunchTypesUiModel,
    onEvent: (LaunchEvents) -> Unit
) {
    when (launchItem) {

        is LaunchTypesUiModel.LaunchUi -> {
            LaunchCard(
                launchItem = launchItem,
                onEvent = {
                //    onEvent(HandleLaunchClickEvent(launchItem.links))
                }
            )
        }
    }
}
