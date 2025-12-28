package com.seancoyle.feature.launch.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.components.toolbar.HomeAppBar
import com.seancoyle.feature.launch.presentation.state.LaunchesEvents
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterial3WindowSizeClassApi
internal class LaunchFragment : Fragment() {

    private val viewModel by viewModels<LaunchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        val windowSizeClass = calculateWindowSizeClass(requireActivity())
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
            ) { padding ->
                Box(
                    Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    LaunchScreen(
                        snackbarHostState = snackbarHostState,
                        windowSizeClass = windowSizeClass,
                    )
                }
            }
        }
    }

}
