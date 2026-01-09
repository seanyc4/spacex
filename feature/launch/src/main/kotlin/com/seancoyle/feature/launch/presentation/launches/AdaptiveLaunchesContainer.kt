package com.seancoyle.feature.launch.presentation.launches

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.ui.adaptive.AdaptiveLayoutConfig
import com.seancoyle.core.ui.adaptive.DetailPlaceholder
import com.seancoyle.core.ui.adaptive.TwoPaneLayout
import com.seancoyle.core.ui.adaptive.rememberAdaptiveLayoutConfig
import com.seancoyle.feature.launch.presentation.launch.LaunchRoute
import com.seancoyle.feature.launch.presentation.launch.LaunchViewModel
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesState

/**
 * Adaptive container for launches that handles layout based on WindowSizeClass.
 * Layout behavior:
 * - Compact/Medium: Single-pane with navigation to detail screen
 * - Expanded: Two-pane with list on left and detail on right
 */
@Composable
fun AdaptiveLaunchesContainer(
    feedState: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    isRefreshing: Boolean,
    windowSizeClass: WindowSizeClass,
    launchViewModel: LaunchViewModel?,
    onEvent: (LaunchesEvents) -> Unit,
    onUpdateScrollPosition: (Int) -> Unit,
    onNavigateToDetail: (String, LaunchesType) -> Unit,
    modifier: Modifier = Modifier
) {
    val layoutConfig = rememberAdaptiveLayoutConfig(windowSizeClass)

    when {
        layoutConfig.showDetailPane -> {
            // Two-pane layout for expanded width
            TwoPaneLaunchesLayout(
                feedState = feedState,
                state = state,
                isRefreshing = isRefreshing,
                windowSizeClass = windowSizeClass,
                layoutConfig = layoutConfig,
                launchViewModel = launchViewModel,
                onEvent = onEvent,
                onUpdateScrollPosition = onUpdateScrollPosition,
                modifier = modifier
            )
        }
        else -> {
            // Single-pane layout with navigation
            LaunchesScreen(
                feedState = feedState,
                state = state,
                isRefreshing = isRefreshing,
                windowSizeClass = windowSizeClass,
                columnCount = layoutConfig.listColumnCount,
                selectedLaunchId = null,
                onEvent = onEvent,
                onUpdateScrollPosition = onUpdateScrollPosition,
                onClick = onNavigateToDetail,
                modifier = modifier
            )
        }
    }
}

/**
 * Two-pane layout with list on left and detail on right.
 */
@Composable
private fun TwoPaneLaunchesLayout(
    feedState: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    isRefreshing: Boolean,
    windowSizeClass: WindowSizeClass,
    layoutConfig: AdaptiveLayoutConfig,
    launchViewModel: LaunchViewModel?,
    onEvent: (LaunchesEvents) -> Unit,
    onUpdateScrollPosition: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TwoPaneLayout(
        listPane = {
            LaunchesScreen(
                feedState = feedState,
                state = state,
                isRefreshing = isRefreshing,
                windowSizeClass = windowSizeClass,
                columnCount = layoutConfig.listColumnCount,
                selectedLaunchId = state.selectedLaunchId,
                onEvent = onEvent,
                onUpdateScrollPosition = onUpdateScrollPosition,
                onClick = { launchId, launchType ->
                    // In two-pane mode, select instead of navigate
                    onEvent(LaunchesEvents.SelectLaunchEvent(launchId, launchType))
                },
                modifier = modifier
            )
        },
        detailPane = {
            if (launchViewModel != null) {
                LaunchRoute(viewModel = launchViewModel)
            }
        },
        showDetailPane = state.selectedLaunchId != null,
        placeholder = {
            DetailPlaceholder()
        },
        modifier = modifier
    )
}

