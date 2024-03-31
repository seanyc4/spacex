package com.seancoyle.launch.implementation.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.seancoyle.core_ui.composables.CircularProgressBar
import com.seancoyle.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.implementation.domain.model.CompanySummary
import com.seancoyle.launch.implementation.domain.model.SectionTitle
import com.seancoyle.launch.implementation.domain.model.ViewCarousel
import com.seancoyle.launch.implementation.domain.model.ViewGrid
import com.seancoyle.launch.implementation.presentation.PaginationState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

private const val GRID_COLUMN_SIZE = 2

@OptIn(ExperimentalMaterialApi::class)
@FlowPreview
@Composable
internal fun LaunchesContent(
    launches: List<ViewType>,
    paginationState: PaginationState,
    page: Int,
    onChangeScrollPosition: (Int) -> Unit,
    loadNextPage: (Int) -> Unit,
    pullRefreshState: PullRefreshState,
    onItemClicked: (links: Links) -> Unit,
    getLaunchStatusIcon: (LaunchStatus) -> Int,
    getLaunchDate: (LaunchDateStatus) -> Int,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyGridState()

    // Observe and save scroll position to view model
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.debounce(500L)
            .collectLatest { position ->
            onChangeScrollPosition(position)
        }
    }

    Box(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .pullRefresh(pullRefreshState)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_COLUMN_SIZE),
            state = listState,
            modifier = modifier.semantics { testTag = "Launch Grid" }
        ) {
            itemsIndexed(
                items = launches,
                span = { _, item ->
                    GridItemSpan(if (item.type == ViewType.TYPE_GRID) 1 else 2)
                }
            ) { index, launchItem ->
                if ((index + 1) >= (page * PAGINATION_PAGE_SIZE)) {
                    LazyVerticalGridPagination(
                        listState = listState,
                        buffer = GRID_COLUMN_SIZE,
                        index = index,
                        loadNextPage = loadNextPage
                    )
                }
                when (launchItem.type) {
                    ViewType.TYPE_SECTION_TITLE -> {
                        LaunchHeading(launchItem as SectionTitle)
                    }

                    ViewType.TYPE_HEADER -> {
                        CompanySummaryCard(launchItem as CompanySummary)
                    }

                    ViewType.TYPE_LIST -> {
                        LaunchCard(
                            launchItem = launchItem as Launch,
                            onClick = { onItemClicked(launchItem.links) },
                            getLaunchStatusIcon = getLaunchStatusIcon(launchItem.launchStatus),
                            getLaunchDate = getLaunchDate(launchItem.launchDateStatus)
                        )
                    }

                    ViewType.TYPE_GRID -> {
                        LaunchGridCard(
                            launchItem = launchItem as ViewGrid,
                            onClick = { onItemClicked(launchItem.links) })
                    }

                    ViewType.TYPE_CAROUSEL -> {
                        val carouselItems = (launchItem as ViewCarousel).items
                        LazyRow {
                            itemsIndexed(carouselItems) { _, carouselItem ->
                                LaunchCarouselCard(
                                    launchItem = carouselItem,
                                    onClick = { onItemClicked(carouselItem.links) })
                            }
                        }
                    }

                    else -> throw ClassCastException("Unknown viewType ${launchItem.type}")
                }

                when (paginationState) {
                    is PaginationState.Loading -> {
                        CircularProgressBar()
                    }
                    is PaginationState.Error -> {}
                    is PaginationState.None -> {}
                }
            }
        }
    }
}