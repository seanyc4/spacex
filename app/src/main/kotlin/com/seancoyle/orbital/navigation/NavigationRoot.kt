package com.seancoyle.orbital.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.seancoyle.feature.launch.presentation.launch.LaunchRoute
import com.seancoyle.feature.launch.presentation.launch.LaunchViewModel
import com.seancoyle.feature.launch.presentation.launches.LaunchesRoute
import com.seancoyle.navigation.Route
import com.seancoyle.navigation.scenes.ListDetailScene
import com.seancoyle.navigation.scenes.rememberListDetailSceneStrategy

@Composable
fun NavigationRoot(
    snackbarHostState: SnackbarHostState,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
    val backStack = rememberNavBackStack(Route.Launches)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategy = listDetailStrategy,
        entryProvider = entryProvider {
            entry<Route.PlaceholderDetail>(metadata = ListDetailScene.detailPane()) {
                if (!isExpandedScreen) {
                    // On compact screens, remove placeholder detail when in portrait mode
                    backStack.removeIf { it is Route.PlaceholderDetail }
                }
                PlaceholderDetailScreen()
            }

            entry<Route.Launches>(metadata = ListDetailScene.listPane()) {
                if (isExpandedScreen) {
                    // On expanded screens, ensure a detail placeholder is present if no detail exists
                    val hasDetail = backStack.any { it is Route.Launch || it is Route.PlaceholderDetail }
                    if (!hasDetail) {
                        backStack.add(Route.PlaceholderDetail)
                    }
                }
                LaunchesRoute(
                    snackbarHostState = snackbarHostState,
                    onNavigateToLaunch = { launchId, launchesType ->
                        backStack.addDetail(Route.Launch(launchId, launchesType))
                    },
                    columnCount = if (isExpandedScreen) 2 else 1,
                    selectedLaunchId = backStack
                        .filterIsInstance<Route.Launch>()
                        .lastOrNull()
                        ?.launchId
                )
            }

            entry<Route.Launch>(metadata = ListDetailScene.detailPane()) { key ->
                val viewModel = hiltViewModel<LaunchViewModel, LaunchViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(
                            launchId = key.launchId,
                            launchType = key.launchesType
                        )
                    }
                )
                LaunchRoute(viewModel = viewModel)
            }
        },
    )
}

private fun NavBackStack<NavKey>.addDetail(detailRoute: Route.Launch) {
    // Remove any existing detail routes (including placeholder), then add the new detail route
    removeIf { it is Route.Launch || it is Route.PlaceholderDetail }
    add(detailRoute)
}
