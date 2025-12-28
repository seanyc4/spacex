package com.seancoyle.feature.launch.implementation.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.seancoyle.core.ui.components.progress.CircularProgressBar
import com.seancoyle.core.ui.designsystem.buttons.ButtonPrimary
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.feature.launch.api.LaunchTestTags.LAUNCH_LAZY_COLUMN
import com.seancoyle.feature.launch.implementation.R
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUi
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScreenState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@Composable
internal fun Launches(
    launches: LazyPagingItems<LaunchUi>,
    screenState: LaunchesScreenState,
    onEvent: (LaunchesEvents) -> Unit,
    onUpdateScrollPosition: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = screenState.scrollPosition)
    ObserveScrollPosition(listState, onUpdateScrollPosition)

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(Dimens.dp8),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                modifier
                    .semantics { testTag = LAUNCH_LAZY_COLUMN }
                    .fillMaxSize()
                    .padding(start = Dimens.dp8, end = Dimens.dp8)
        ) {
            item {
                if (launches.loadState.prepend is LoadState.Error) {
                    ButtonPrimary(
                        text = stringResource(R.string.retry),
                        onClick = { onEvent(LaunchesEvents.RetryFetchEvent) },
                        modifier = Modifier.padding(vertical = Dimens.dp8)
                    )
                }
            }
            items(
                count = launches.itemCount,
                key = launches.itemKey { item ->
                    when (item) {
                        is LaunchUi -> item.id
                    }
                },
            ) { index ->
                val launchItem = launches[index]
                if (launchItem != null) {
                    LaunchCard(
                        launchItem = launchItem,
                        onEvent = {}
                    )
                }
            }
            item {
                val appendLoadState = launches.loadState.mediator?.append ?: launches.loadState.append
                if (appendLoadState is LoadState.Loading) {
                    CircularProgressBar()
                }
                if (appendLoadState is LoadState.Error) {
                    ButtonPrimary(
                        text = stringResource(R.string.retry),
                        onClick = { onEvent(LaunchesEvents.RetryFetchEvent) },
                        modifier = Modifier.padding(vertical = Dimens.dp8)
                    )
                }
            }
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun ObserveScrollPosition(
    listState: LazyListState,
    onUpdateScrollPosition: (Int) -> Unit,
) {
    // Observe and save scroll position to ViewModel
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.debounce(750L)
            .collectLatest { position ->
                onUpdateScrollPosition(position)
            }
    }
}
