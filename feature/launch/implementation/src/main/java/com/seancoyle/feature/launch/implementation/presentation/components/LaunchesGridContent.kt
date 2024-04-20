package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.seancoyle.core.ui.composables.CircularProgressBar
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.HandleLaunchClickEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents.SaveScrollPositionEvent
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScrollState
import com.seancoyle.feature.launch.implementation.presentation.state.PaginationState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

private const val GRID_COLUMN_SIZE = 2

@ExperimentalMaterialApi
@FlowPreview
@Composable
internal fun LaunchesGridContent(
    launches: List<LaunchTypes>,
    paginationState: PaginationState,
    scrollState: LaunchesScrollState,
    onEvent: (LaunchEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyGridState()
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
            itemsIndexed(
                items = launches,
                span = { _, item ->
                    GridItemSpan(if (item is LaunchTypes.Grid) 1 else 2)
                }
            ) { index, launchItem ->

                RenderGridSections(
                    launchItem = launchItem,
                    onEvent = onEvent
                )

                HandlePagination(
                    index = index,
                    page = scrollState.page,
                    listState = listState,
                    onEvent = onEvent
                )

                PaginationState(paginationState)
            }
        }
    }
}

@Composable
private fun RenderGridSections(
    launchItem: LaunchTypes,
    onEvent: (LaunchEvents) -> Unit
) {
    when (launchItem) {
        is LaunchTypes.SectionTitle -> LaunchHeading(launchItem)

        is LaunchTypes.CompanySummary -> CompanySummaryCard(launchItem.company.summary)

        is LaunchTypes.Launch -> {
            LaunchCard(
                launchItem = launchItem,
                onEvent = { onEvent(HandleLaunchClickEvent(launchItem.links)) }
            )
        }

        is LaunchTypes.Grid -> {
            LaunchGridCard(
                launchItem = launchItem,
                onEvent = { onEvent(HandleLaunchClickEvent(launchItem.items.links)) },
            )
        }

        is LaunchTypes.Carousel -> {
            LazyRow {
                itemsIndexed(launchItem.items) { _, carouselItem ->
                    LaunchCarouselCard(
                        launchItem = carouselItem,
                        onEvent = { onEvent(HandleLaunchClickEvent(carouselItem.links)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HandlePagination(
    index: Int,
    page: Int,
    listState: LazyGridState,
    onEvent: (LaunchEvents) -> Unit
) {
    if ((index + 1) >= (page * PAGINATION_PAGE_SIZE)) {
        LazyVerticalGridPagination(
            listState = listState,
            buffer = GRID_COLUMN_SIZE,
            index = index,
            onEvent = onEvent
        )
    }
}

@Composable
private fun PaginationState(paginationState: PaginationState) {
    when (paginationState) {
        is PaginationState.Loading -> CircularProgressBar()
        is PaginationState.Error -> {}
        is PaginationState.None -> {}
    }
}

@FlowPreview
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