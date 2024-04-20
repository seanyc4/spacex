package com.seancoyle.feature.launch.implementation.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import com.seancoyle.core.ui.extensions.adaptiveHorizontalPadding
import com.seancoyle.core.ui.theme.AppTheme
import com.seancoyle.feature.launch.implementation.presentation.components.HomeAppBar
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@ExperimentalMaterial3WindowSizeClassApi
internal class LaunchFragment : Fragment() {

    private val viewModel by viewModels<LaunchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        val windowSize = calculateWindowSizeClass(requireActivity())
        val isLandscape = windowSize.heightSizeClass == WindowHeightSizeClass.Compact
        val snackbarHostState = remember { SnackbarHostState() }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = false,
            onRefresh = { viewModel.swipeToRefresh() }
        )

        AppTheme {
            Scaffold(
                topBar = {
                    HomeAppBar(
                        onClick = {
                            viewModel.setDialogFilterDisplayedState(true)
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
                        .adaptiveHorizontalPadding(isLandscape)
                        .padding(padding)
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState)
                ) {
                    LaunchRoute(
                        viewModel = viewModel,
                        pullRefreshState = pullRefreshState,
                        snackbarHostState = snackbarHostState,
                        isLandscape = isLandscape,
                        openLink = { launchIntent(it) },
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState()
        super.onSaveInstanceState(outState)
    }

    // Load url link in external browser
    private fun launchIntent(url: String?) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url.toString())
                )
            )
        } catch (e: ActivityNotFoundException) {
            viewModel.displayUnableToLoadLinkError()
        }
    }
}