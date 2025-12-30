package com.seancoyle.spacex.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.seancoyle.feature.launch.presentation.launches.LaunchesScreen
import com.seancoyle.feature.launch.presentation.launches.LaunchesViewModel
import com.seancoyle.navigation.Route

@Composable
fun NavigationRoot(
    viewModel: LaunchesViewModel,
    snackbarHostState: SnackbarHostState,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(
        Route.LaunchList
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
                is Route.LaunchList -> {
                    NavEntry(key) {
                        LaunchesScreen(
                            viewModel = viewModel,
                            snackbarHostState = snackbarHostState,
                            windowSizeClass = windowSizeClass
                        )
                    }
                }

                else -> error("Unknown NavKey: $key")
            }
        },
    )
}