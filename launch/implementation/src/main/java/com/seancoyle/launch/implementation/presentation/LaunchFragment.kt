package com.seancoyle.launch.implementation.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.navigation.fragment.findNavController
import com.seancoyle.core.presentation.NotificationState
import com.seancoyle.core.presentation.NotificationType
import com.seancoyle.core.presentation.NotificationUiType
import com.seancoyle.core.presentation.asStringResource
import com.seancoyle.core_ui.extensions.adaptiveHorizontalPadding
import com.seancoyle.core_ui.theme.AppTheme
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.presentation.components.HomeAppBar
import com.seancoyle.launch.implementation.presentation.state.LaunchEvents
import com.seancoyle.launch.implementation.presentation.state.LaunchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

const val LINKS_KEY = "links"

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3WindowSizeClassApi
internal class LaunchFragment : Fragment() {

    private val launchViewModel by viewModels<LaunchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {

        val windowSize = calculateWindowSizeClass(requireActivity())
        val snackbarHostState = remember { SnackbarHostState() }

        val refreshing = rememberPullRefreshState(
            refreshing = false,
            onRefresh = {
                launchViewModel.clearQueryParameters()
                launchViewModel.clearListState()
                launchViewModel.setEvent(LaunchEvents.GetCompanyApiAndCacheEvent)
            }
        )

        AppTheme {
            Scaffold(
                topBar = {
                    HomeAppBar(
                        onClick = {
                            launchViewModel.setDialogFilterDisplayedState(true)
                        }
                    )
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { padding ->
                Box(
                    Modifier
                        .adaptiveHorizontalPadding(windowSize)
                        .padding(padding)
                ) {
                    LaunchRoute(
                        viewModel = launchViewModel,
                        refreshState = refreshing,
                        snackbarHostState = snackbarHostState,
                        onItemClicked = ::onCardClicked,
                        windowSize = windowSize
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get result from bottom action sheet fragment
        setFragmentResultListener(LINKS_KEY) { key, bundle ->
            if (key == LINKS_KEY) {
                launchIntent(bundle.getString(LINKS_KEY))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        launchViewModel.saveState()
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
            displayUnableToLoadLinkError()
        }
    }

    private fun onCardClicked(links: Links) {
        if (isLinksNullOrEmpty(links)) {
            displayNoLinksError()
        } else {
            displayBottomActionSheet(links)
        }
    }

    private fun isLinksNullOrEmpty(links: Links) =
        links.articleLink.isNullOrEmpty() &&
                links.webcastLink.isNullOrEmpty() &&
                links.wikiLink.isNullOrEmpty()

    private fun displayBottomActionSheet(links: Links) {
        if (findNavController().currentDestination?.id == R.id.launchFragment) {
            findNavController().navigate(
                R.id.action_launchFragment_to_launchBottomActionSheet,
                bundleOf(LINKS_KEY to links)
            )
        }
    }

    private fun displayNoLinksError() {
        launchViewModel.setEvent(
            event = LaunchEvents.NotificationEvent(
                notificationState = NotificationState(
                    notificationType = NotificationType.Info,
                    message = R.string.no_links.asStringResource(),
                    notificationUiType = NotificationUiType.Dialog
                ),
            )
        )
    }

    private fun displayUnableToLoadLinkError() {
        launchViewModel.setEvent(
            event = LaunchEvents.NotificationEvent(
                notificationState = NotificationState(
                    notificationType = NotificationType.Error,
                    message = R.string.error_links.asStringResource(),
                    notificationUiType = NotificationUiType.Snackbar
                )
            )
        )
    }

}