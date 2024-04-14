package com.seancoyle.feature.launch.implementation.presentation

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
import com.seancoyle.core.ui.StringResource
import com.seancoyle.core.ui.composables.CircularProgressBar
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.implementation.presentation.components.CompanySummaryCard
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchCard
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchCarouselCard
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchGridCard
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchHeading
import com.seancoyle.feature.launch.implementation.presentation.components.LazyVerticalGridPagination
import com.seancoyle.feature.launch.implementation.presentation.state.PaginationState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

private const val GRID_COLUMN_SIZE = 2

@ExperimentalMaterialApi
@FlowPreview
@Composable
internal fun LaunchesContent(
    launches: List<LaunchTypes>,
    paginationState: PaginationState,
    page: Int,
    onChangeScrollPosition: (Int) -> Unit,
    loadNextPage: (Int) -> Unit,
    onItemClicked: (links: Links) -> Unit,
    getLaunchStatusIcon: (LaunchStatus) -> Int,
    getLaunchDate: (LaunchDateStatus) -> Int,
    getCompanySummary: (Company) -> StringResource,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyGridState()
    ObserveScrollPosition(listState, onChangeScrollPosition)

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
                    getCompanySummary = getCompanySummary,
                    onItemClicked = onItemClicked,
                    getLaunchStatusIcon = getLaunchStatusIcon,
                    getLaunchDate = getLaunchDate
                )

                HandlePagination(
                    index = index,
                    page = page,
                    listState = listState,
                    loadNextPage = loadNextPage
                )

                PaginationState(paginationState)
            }
        }
    }
}

@Composable
private fun RenderGridSections(
    launchItem: LaunchTypes,
    getCompanySummary: (Company) -> StringResource,
    onItemClicked: (links: Links) -> Unit,
    getLaunchStatusIcon: (LaunchStatus) -> Int,
    getLaunchDate: (LaunchDateStatus) -> Int
) {
    when (launchItem) {
        is LaunchTypes.SectionTitle -> LaunchHeading(launchItem)

        is LaunchTypes.CompanySummary -> CompanySummaryCard(getCompanySummary(launchItem.company).asString())

        is LaunchTypes.Launch -> {
            LaunchCard(
                launchItem = launchItem,
                onClick = { onItemClicked(launchItem.links) },
                getLaunchStatusIcon = getLaunchStatusIcon(launchItem.launchStatus),
                getLaunchDate = getLaunchDate(launchItem.launchDateStatus)
            )
        }

        is LaunchTypes.Grid -> {
            LaunchGridCard(
                launchItem = launchItem,
                onClick = { onItemClicked(launchItem.links) })
        }

        is LaunchTypes.Carousel -> {
            LazyRow {
                itemsIndexed(launchItem.items) { _, carouselItem ->
                    LaunchCarouselCard(
                        launchItem = carouselItem,
                        onClick = { onItemClicked(carouselItem.links) })
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
    loadNextPage: (Int) -> Unit
) {
    if ((index + 1) >= (page * PAGINATION_PAGE_SIZE)) {
        LazyVerticalGridPagination(
            listState = listState,
            buffer = GRID_COLUMN_SIZE,
            index = index,
            loadNextPage = loadNextPage
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
    onChangeScrollPosition: (Int) -> Unit
) {
    // Observe and save scroll position to view model
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.debounce(750L)
            .collectLatest { position ->
                onChangeScrollPosition(position)
            }
    }
}