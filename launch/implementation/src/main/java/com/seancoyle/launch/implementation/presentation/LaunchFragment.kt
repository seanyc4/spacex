package com.seancoyle.launch.implementation.presentation

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.presentation.asStringResource
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch.api.LaunchConstants.ORDER_ASC
import com.seancoyle.launch.api.LaunchConstants.ORDER_DESC
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.presentation.composables.HomeAppBar
import com.seancoyle.launch.implementation.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

const val LINKS_KEY = "links"

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
internal class LaunchFragment : Fragment() {

    private val launchViewModel by viewModels<LaunchViewModel>()

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {

        val snackbarHostState = remember { SnackbarHostState() }
        val scaffoldState = rememberScaffoldState()
        val refreshing = rememberPullRefreshState(
            refreshing = false,
            onRefresh = {
                launchViewModel.clearQueryParameters()
                launchViewModel.clearListState()
                launchViewModel.setEvent(LaunchEvents.GetCompanyApiAndCacheEvent)
            }
        )

        AppTheme(
            darkTheme = false,
            displayProgressBar = false,
        ) {
            Scaffold(
                topBar = {
                    HomeAppBar(
                        onClick = {
                            launchViewModel.setDialogFilterDisplayedState(true)
                            displayFilterDialog()
                        }
                    )
                },
                scaffoldState = scaffoldState,
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { padding ->
                LaunchRoute(
                    viewModel = launchViewModel,
                    refreshState = refreshing,
                    snackbarHostState = snackbarHostState,
                    onItemClicked = { link ->
                        onCardClicked(link)
                    }
                )
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

    override fun onPause() {
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        launchViewModel.saveState()
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        /* if (launchViewModel.uiState.value.mergedLaunches.isNullOrEmpty()) {
             launchViewModel.newSearchEvent()
         }*/
        if (launchViewModel.getIsDialogFilterDisplayedState()) {
            displayFilterDialog()
        }
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
            displayErrorDialogUnableToLoadLink()
        }
    }

    private fun onCardClicked(links: Links) {
        if (isLinksNullOrEmpty(links)) {
            displayErrorDialogNoLinks()
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

    private fun displayFilterDialog() {

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .onDismiss { launchViewModel.setDialogFilterDisplayedState(false) }
                .customView(R.layout.dialog_filter)
                .cornerRadius(res = R.dimen.default_corner_radius)

            val view = dialog.getCustomView()
            val order = launchViewModel.getOrderState()
            val filter = launchViewModel.getFilterState()
            var newOrder: String? = null

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    LaunchStatus.ALL -> check(R.id.filter_all)
                    LaunchStatus.SUCCESS -> check(R.id.filter_success)
                    LaunchStatus.FAILED -> check(R.id.filter_failure)
                    LaunchStatus.UNKNOWN -> check(R.id.filter_unknown)
                }
            }

            // set switch to on/off based on state
            val orderSwitch = view.findViewById<SwitchMaterial>(R.id.order_switch).apply {
                when (order) {
                    ORDER_ASC -> isChecked = false
                    ORDER_DESC -> isChecked = true
                }
            }

            orderSwitch.setOnCheckedChangeListener { _, isChecked ->
                newOrder = if (isChecked) {
                    ORDER_DESC
                } else {
                    ORDER_ASC
                }
            }

            view.findViewById<TextView>(R.id.apply_btn).setOnClickListener {

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_success -> LaunchStatus.SUCCESS
                        R.id.filter_failure -> LaunchStatus.FAILED
                        R.id.filter_unknown -> LaunchStatus.UNKNOWN
                        R.id.filter_all -> LaunchStatus.ALL
                        else -> null
                    }

                val yearQuery = view.findViewById<EditText>(R.id.year_query).text.toString()

                // Save data to view model
                launchViewModel.apply {
                    newOrder?.let { order ->
                        setLaunchOrderState(order)
                    }
                    newFilter?.let { filter ->
                        setLaunchFilterState(filter)
                    }
                    setYearState(yearQuery)
                }

                startNewSearch()
                dialog.dismiss()
            }

            view.findViewById<TextView>(R.id.cancel_btn).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun startNewSearch() {
        printLogDebug("EventExecutor", "start new search")
        launchViewModel.clearListState()
        launchViewModel.newSearch()
    }

    private fun displayErrorDialogNoLinks() {
        launchViewModel.setEvent(
            event = LaunchEvents.CreateMessageEvent(
                response = Response(
                    messageType = MessageType.Info,
                    message = R.string.no_links.asStringResource(),
                    messageDisplayType = MessageDisplayType.Dialog
                )
            )
        )
    }

    private fun displayErrorDialogUnableToLoadLink() {
        launchViewModel.setEvent(
            event = LaunchEvents.CreateMessageEvent(
                response = Response(
                    messageType = MessageType.Error,
                    message = R.string.error_links.asStringResource(),
                    messageDisplayType = MessageDisplayType.Dialog
                )
            )
        )
    }

}