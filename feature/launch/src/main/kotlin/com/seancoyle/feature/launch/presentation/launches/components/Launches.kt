package com.seancoyle.feature.launch.presentation.launches.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.test.testags.LaunchesTestTags.LAUNCHES_SCREEN
import com.seancoyle.core.ui.components.progress.CircularProgressBar
import com.seancoyle.core.ui.designsystem.buttons.ButtonPrimary
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingMedium
import com.seancoyle.core.ui.util.ObserveScrollPosition
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesState

@Composable
internal fun Launches(
    launches: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    columnCount: Int,
    selectedLaunchId: String?,
    onEvent: (LaunchesEvents) -> Unit,
    onUpdateScrollPosition: (Int) -> Unit,
    onClick: (String, LaunchesType) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState(initialFirstVisibleItemIndex = state.scrollPosition)
    ObserveScrollPosition(gridState, onUpdateScrollPosition)

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(columnCount),
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingMedium),
        horizontalArrangement = Arrangement.spacedBy(paddingMedium),
        contentPadding = PaddingValues(
            start = paddingMedium,
            end = paddingMedium,
            top = paddingSmall,
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ),
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .testTag(LAUNCHES_SCREEN)
            .semantics { contentDescription = LAUNCHES_SCREEN }
    ) {
        // Prepend loading/error state (full width)
        item(span = { GridItemSpan(maxLineSpan) }) {
            if (launches.loadState.prepend is LoadState.Error) {
                ButtonPrimary(
                    text = stringResource(R.string.retry),
                    onClick = { onEvent(LaunchesEvents.RetryFetchEvent) },
                    modifier = Modifier.padding(vertical = paddingMedium)
                )
            }
        }

        items(
            count = launches.itemCount,
            key = launches.itemKey { it.id }
        ) { index ->
            val launchItem = launches[index]
            if (launchItem != null) {
                val isSelected = launchItem.id == selectedLaunchId
                LaunchCard(
                    launchItem = launchItem,
                    onClick = onClick,
                    launchesType = state.launchesType,
                    isSelected = isSelected
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            val appendLoadState = launches.loadState.mediator?.append ?: launches.loadState.append
            if (appendLoadState is LoadState.Loading) {
                CircularProgressBar()
            }
            if (appendLoadState is LoadState.Error) {
                ButtonPrimary(
                    text = stringResource(R.string.retry),
                    onClick = { onEvent(LaunchesEvents.RetryFetchEvent) },
                    modifier = Modifier.padding(paddingSmall)
                )
            }
        }
    }
}
