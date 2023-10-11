package com.seancoyle.launch.implementation.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch.api.LaunchNetworkConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.launch.api.domain.model.CompanySummary
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.SectionTitle
import com.seancoyle.launch.api.domain.model.ViewCarousel
import com.seancoyle.launch.api.domain.model.ViewGrid
import com.seancoyle.launch.api.domain.model.ViewType

private const val GRID_COLUMN_SIZE = 2

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LaunchesContent(
    launches: List<ViewType>,
    onChangeScrollPosition: (Int) -> Unit,
    page: Int,
    loadNextPage: () -> Unit,
    pullRefreshState: PullRefreshState,
    modifier: Modifier = Modifier,
    onCardClicked: (links: Links) -> Unit
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .pullRefresh(pullRefreshState)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_COLUMN_SIZE),
            modifier = modifier.semantics { testTag = "Launch Grid" }
        ) {
            itemsIndexed(
                items = launches,
                span = { _, item ->
                    GridItemSpan(if (item.type == ViewType.TYPE_GRID) 1 else 2)
                }
            ) { index, launchItem ->
                printLogDebug("SPACEXAPP: LazyVerticalGrid", ": index = $index")
                onChangeScrollPosition(index)
                if ((index + 1) >= (page * PAGINATION_PAGE_SIZE)) {
                    printLogDebug("SPACEXAPP: LazyVerticalGrid", "index + 1 = ${index + 1}")
                    printLogDebug("SPACEXAPP: LazyVerticalGrid", "page * PAGINATION_PAGE_SIZE = ${page * PAGINATION_PAGE_SIZE}")
                    printLogDebug("SPACEXAPP: LazyVerticalGrid", "loadNextPage()")
                    loadNextPage()
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
                            onClick = { onCardClicked(launchItem.links) }
                        )
                       // printLogDebug("SPACEXAPP: LazyVerticalGrid", launchItem.missionName)

                    }

                    ViewType.TYPE_GRID -> {
                        LaunchGridCard(
                            launchItem = launchItem as ViewGrid,
                            onClick = { onCardClicked(launchItem.links) })
                    }

                    ViewType.TYPE_CAROUSEL -> {
                        val carouselItems = (launchItem as ViewCarousel).items
                        LazyRow {
                            itemsIndexed(carouselItems) { index, carouselItem ->
                                LaunchCarouselCard(
                                    launchItem = carouselItem,
                                    onClick = { onCardClicked(carouselItem.links) })
                             //   printLogDebug("SPACEXAPP: Recyclerview - ROW ", ": index${index}")
                            }
                        }
                    }

                    else -> throw ClassCastException("Unknown viewType ${launchItem.type}")
                }
            }
        }
        PullRefreshIndicator(
            refreshing = false,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}