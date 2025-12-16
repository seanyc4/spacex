package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.seancoyle.core.ui.composables.CircularProgressBar
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchTypesUiModel
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.SaveScrollPositionEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScrollState
import com.seancoyle.feature.launch.implementation.presentation.state.PaginationState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

private const val GRID_COLUMN_SIZE = 2

@Composable
internal fun LaunchesGridContent(
    launches: List<LaunchTypesUiModel>,
    paginationState: PaginationState,
    scrollState: LaunchesScrollState,
    onEvent: (LaunchEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyGridState()
    ObserveScrollPosition(listState, onEvent)

    // Render pagination detection once, outside the items
    LazyVerticalGridPagination(
        listState = listState,
        buffer = GRID_COLUMN_SIZE,
        onEvent = onEvent
    )

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_COLUMN_SIZE),
            state = listState,
            modifier = modifier.semantics { testTag = "Launch Grid" }
        ) {
            itemsIndexed(
                items = launches,
                key = { _, item ->
                    when (item) {
                        is LaunchTypesUiModel.LaunchUi -> item.id
                    }
                },
                span = { _, item ->
                    GridItemSpan(if (item !is LaunchTypesUiModel.LaunchUi) 1 else 2)
                }
            ) { _, launchItem ->

                RenderGridSections(
                    launchItem = launchItem,
                    onEvent = onEvent
                )

                PaginationState(paginationState)
            }
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


@Composable
private fun PaginationState(paginationState: PaginationState) {
    when (paginationState) {
        is PaginationState.Loading -> CircularProgressBar()
        is PaginationState.Error -> {}
        is PaginationState.Idle -> {}
        is PaginationState.EndReached -> {}
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
                onEvent(SaveScrollPositionEvent(position))
            }
    }
}
