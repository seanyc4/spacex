package com.seancoyle.feature.launch.presentation.launches.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.ui.components.progress.CircularProgressBar
import com.seancoyle.core.ui.designsystem.buttons.ButtonPrimary
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingMedium
import com.seancoyle.core.ui.designsystem.theme.Dimens.paddingSmall
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingMedium
import com.seancoyle.core.ui.util.ObserveScrollPosition
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.LaunchesTestTags
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesState

@Composable
internal fun Launches(
    launches: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    onEvent: (LaunchesEvents) -> Unit,
    onUpdateScrollPosition: (Int) -> Unit,
    onClick: (String, LaunchesType) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = state.scrollPosition)
    ObserveScrollPosition(listState, onUpdateScrollPosition)

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(
            start = paddingMedium,
            end = paddingMedium,
            top = paddingSmall,
            bottom = 24.dp
        ),
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .semantics { testTag = LaunchesTestTags.LAUNCH_LAZY_COLUMN }
    ) {
        item {
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
                LaunchCard(
                    launchItem = launchItem,
                    onClick = onClick,
                    launchesType = state.launchesType,
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
                    modifier = Modifier.padding(paddingSmall)
                )
            }
        }
    }
}
