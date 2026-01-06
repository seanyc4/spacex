package com.seancoyle.spacex.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.spacex.navigation.NavigationRoot

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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
                snackbarHostState = snackbarHostState,
                windowSizeClass = windowSizeClass
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@PreviewDarkLightMode
@Composable
private fun AppPreview() {
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
    AppTheme {
        App(windowSizeClass = windowSizeClass)
    }
}
