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
import com.seancoyle.feature.launch.presentation.LaunchScreen
import com.seancoyle.navigation.Route

@Composable
fun NavigationRoot(
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
                        LaunchScreen(
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