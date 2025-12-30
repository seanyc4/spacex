package com.seancoyle.spacex.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.ui.components.toolbar.HomeAppBar
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launches.LaunchViewModel
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents
import com.seancoyle.spacex.navigation.NavigationRoot
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
@ExperimentalMaterial3WindowSizeClassApi
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var crashlytics: Crashlytics

    private val viewModel by viewModels<LaunchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // add google-services.json before init in prod app
        //crashlytics.init(true)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val snackbarHostState = remember { SnackbarHostState() }

            AppTheme {
                Scaffold(
                    topBar = {
                        HomeAppBar(
                            onClick = {
                                viewModel.onEvent(LaunchesEvents.DisplayFilterDialogEvent)
                            }
                        )
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier.semantics {
                        testTagsAsResourceId = true
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    NavigationRoot(
                        viewModel = viewModel,
                        snackbarHostState = snackbarHostState,
                        windowSizeClass = windowSizeClass,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

}
