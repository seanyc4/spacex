package com.seancoyle.orbital.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import com.seancoyle.feature.launch.presentation.launch.LaunchRoute
import com.seancoyle.feature.launch.presentation.launch.LaunchViewModel
import com.seancoyle.feature.launch.presentation.launches.LaunchesRoute
import com.seancoyle.navigation.Route
import com.seancoyle.navigation.scenes.ListDetailScene

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NavigationRoot(
    snackbarHostState: SnackbarHostState,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
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
            entry<Route.Launches>(
                metadata = ListDetailScene.listPane()
            ) {
                LaunchesRoute(
                    snackbarHostState = snackbarHostState,
                    windowSizeClass = windowSizeClass,
                    onNavigateToLaunch = { launchId, launchesType ->
                        backStack.addDetail(Route.Launch(launchId, launchesType))
                    }
                )
            }
            entry<Route.Launch>(
                metadata = ListDetailScene.detailPane()
            ) { key ->
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
    // Remove any existing detail routes, then add the new detail route
    removeIf { it is Route.Launch }
    add(detailRoute)
}
