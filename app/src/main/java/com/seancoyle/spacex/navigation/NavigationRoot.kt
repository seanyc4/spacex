package com.seancoyle.spacex.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.seancoyle.feature.launch.presentation.launch.LaunchScreen
import com.seancoyle.feature.launch.presentation.launch.LaunchViewModel
import com.seancoyle.feature.launch.presentation.launches.LaunchesRoute
import com.seancoyle.navigation.Route

@Composable
fun NavigationRoot(
    snackbarHostState: SnackbarHostState,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(
        Route.Launches
    )
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                is Route.Launches -> {
                    NavEntry(key) {
                        LaunchesRoute(
                            snackbarHostState = snackbarHostState,
                            windowSizeClass = windowSizeClass,
                            onNavigateToLaunch = { launchId, launchesType ->
                                backStack.add(Route.Launch(launchId, launchesType))
                            }
                        )
                    }
                }
                is Route.Launch -> {
                    NavEntry(key) {
                        val viewModel = hiltViewModel<LaunchViewModel, LaunchViewModel.Factory>(
                            creationCallback = { factory ->
                                factory.create(
                                    launchId = key.launchId,
                                    launchType = key.launchesType
                                )
                            }
                        )
                        LaunchScreen(viewModel = viewModel)
                    }
                }

                else -> error("Unknown NavKey: $key")
            }
        },
    )
}
