package com.seancoyle.orbital.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.window.core.layout.WindowSizeClass
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.orbital.navigation.NavigationRoot

@Composable
fun App(windowSizeClass: WindowSizeClass) {
    val snackbarHostState = remember { SnackbarHostState() }

    AppTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            NavigationRoot(
                windowSizeClass = windowSizeClass
            )
        }
    }
}
